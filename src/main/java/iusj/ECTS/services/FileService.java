package iusj.ECTS.services;

import iusj.ECTS.enumerations.ClassLevel;
import iusj.ECTS.enumerations.FileCategory;
import iusj.ECTS.enumerations.Semester;
import iusj.ECTS.models.AcademicFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    AcademicFile UploadPv(MultipartFile excelFile, AcademicFile academicFile);
    List<AcademicFile> AllPvs(FileCategory category, ClassLevel classLevel, Semester semester, String academicYear);
    void deletePv(Long id);
    AcademicFile updatePv(MultipartFile excelFile, AcademicFile academicFile);
}
