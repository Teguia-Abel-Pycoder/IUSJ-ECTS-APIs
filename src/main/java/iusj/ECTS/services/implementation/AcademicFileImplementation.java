package iusj.ECTS.services.implementation;

import iusj.ECTS.enumerations.ClassLevel;
import iusj.ECTS.enumerations.FileCategory;
import iusj.ECTS.enumerations.Semester;
import iusj.ECTS.models.AcademicFile;
import iusj.ECTS.repositories.AcademicFileRepository;
import iusj.ECTS.services.FileHandler;
import iusj.ECTS.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AcademicFileImplementation implements FileService {

    @Autowired
    private FileHandler fileHandler;

    @Autowired
    private AcademicFileRepository academicFileRepository;

    @Override
    @Transactional
    public AcademicFile UploadPv(MultipartFile excelFile, AcademicFile academicFile) {
        validateFileUpload(excelFile);

        academicFile.setFilePath(fileHandler.handleFileUpload(excelFile));
        checkForDuplicatePv(academicFile);

        return academicFileRepository.save(academicFile);
    }

    @Override
    public List<AcademicFile> AllPvs(FileCategory category, ClassLevel classLevel, Semester semester, String academicYear) {
        // Filter based on the provided parameters
        if (category != null && classLevel != null && semester != null && academicYear != null) {
            return academicFileRepository.findByCategoryAndClassLevelAndSemesterAndAcademicYear(category, classLevel, semester, academicYear);
        }
        // If category is provided but others aren't, filter by category only
        if (category != null) {
            return academicFileRepository.findByCategory(category);
        }
        // Similarly, filter by classLevel, semester, or academicYear if they are provided
        if (classLevel != null) {
            return academicFileRepository.findByClassLevel(classLevel);
        }
        if (semester != null) {
            return academicFileRepository.findBySemester(semester);
        }
        if (academicYear != null) {
            return academicFileRepository.findByAcademicYear(academicYear);
        }
        // If no parameters are provided, return all files
        return academicFileRepository.findAll();
    }




    @Override
    @Transactional
    public void deletePv(Long id) {
        if (!academicFileRepository.existsById(id)) {
            throw new RuntimeException("PV not found with id: " + id);
        }
        academicFileRepository.deleteById(id);
    }

    @Override
    @Transactional
    public AcademicFile updatePv(MultipartFile excelFile, AcademicFile academicFile) {
        AcademicFile existingAcademicFile = academicFileRepository.findById(academicFile.getFileId())
                .orElseThrow(() -> new RuntimeException("PV not found with id: " + academicFile.getFileId()));

        if (excelFile != null && !excelFile.isEmpty()) {
            validateFileUpload(excelFile);
            academicFile.setFilePath(fileHandler.handleFileUpload(excelFile));
        }

        existingAcademicFile.setFilePath(academicFile.getFilePath());
        existingAcademicFile.setClassLevel(academicFile.getClassLevel());
        existingAcademicFile.setSemester(academicFile.getSemester());
        existingAcademicFile.setAcademicYear(academicFile.getAcademicYear());
        existingAcademicFile.setUploadedBy(academicFile.getUploadedBy());
        existingAcademicFile.setCategory(academicFile.getCategory());

        checkForDuplicatePv(existingAcademicFile);
        return academicFileRepository.save(existingAcademicFile);
    }

    // Helper methods
    private void validateFileUpload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file cannot be null or empty.");
        }
    }

    private void checkForDuplicatePv(AcademicFile academicFile) {
        Optional<AcademicFile> existingPv = academicFileRepository.findPvByAcademicYearAndClassLevelAndSemesterAndCategory(
                academicFile.getAcademicYear(), academicFile.getClassLevel(), academicFile.getSemester(), academicFile.getCategory());

        if (existingPv.isPresent() && !existingPv.get().getFileId().equals(academicFile.getFileId())) {
            throw new RuntimeException("A PV with the same class level, semester, academic and category year already exists.");
        }
    }
}
