package iusj.ECTS.services.implementation;

import iusj.ECTS.enumerations.ClassLevel;
import iusj.ECTS.enumerations.FileCategory;
import iusj.ECTS.enumerations.Semester;
import iusj.ECTS.models.File;
import iusj.ECTS.repositories.FileRepository;
import iusj.ECTS.services.FileHandler;
import iusj.ECTS.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class FileImplementation implements FileService {

    @Autowired
    private FileHandler fileHandler;

    @Autowired
    private FileRepository fileRepository;

    @Override
    @Transactional
    public File UploadPv(MultipartFile excelFile, File file) {
        validateFileUpload(excelFile);

        file.setFilePath(fileHandler.handleFileUpload(excelFile));
        checkForDuplicatePv(file);

        return fileRepository.save(file);
    }

    @Override
    public List<File> AllPvs(FileCategory category, ClassLevel classLevel, Semester semester, String academicYear) {
        // Filter based on the provided parameters
        if (category != null && classLevel != null && semester != null && academicYear != null) {
            return fileRepository.findByCategoryAndClassLevelAndSemesterAndAcademicYear(category, classLevel, semester, academicYear);
        }
        // If category is provided but others aren't, filter by category only
        if (category != null) {
            return fileRepository.findByCategory(category);
        }
        // Similarly, filter by classLevel, semester, or academicYear if they are provided
        if (classLevel != null) {
            return fileRepository.findByClassLevel(classLevel);
        }
        if (semester != null) {
            return fileRepository.findBySemester(semester);
        }
        if (academicYear != null) {
            return fileRepository.findByAcademicYear(academicYear);
        }
        // If no parameters are provided, return all files
        return fileRepository.findAll();
    }




    @Override
    @Transactional
    public void deletePv(Long id) {
        if (!fileRepository.existsById(id)) {
            throw new RuntimeException("PV not found with id: " + id);
        }
        fileRepository.deleteById(id);
    }

    @Override
    @Transactional
    public File updatePv(MultipartFile excelFile, File file) {
        File existingFile = fileRepository.findById(file.getFileId())
                .orElseThrow(() -> new RuntimeException("PV not found with id: " + file.getFileId()));

        if (excelFile != null && !excelFile.isEmpty()) {
            validateFileUpload(excelFile);
            file.setFilePath(fileHandler.handleFileUpload(excelFile));
        }

        existingFile.setFilePath(file.getFilePath());
        existingFile.setClassLevel(file.getClassLevel());
        existingFile.setSemester(file.getSemester());
        existingFile.setAcademicYear(file.getAcademicYear());
        existingFile.setUploadedBy(file.getUploadedBy());
        existingFile.setCategory(file.getCategory());

        checkForDuplicatePv(existingFile);
        return fileRepository.save(existingFile);
    }

    // Helper methods
    private void validateFileUpload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file cannot be null or empty.");
        }
    }

    private void checkForDuplicatePv(File file) {
        Optional<File> existingPv = fileRepository.findPvByAcademicYearAndClassLevelAndSemesterAndCategory(
                file.getAcademicYear(), file.getClassLevel(), file.getSemester(), file.getCategory());

        if (existingPv.isPresent() && !existingPv.get().getFileId().equals(file.getFileId())) {
            throw new RuntimeException("A PV with the same class level, semester, academic and category year already exists.");
        }
    }
}
