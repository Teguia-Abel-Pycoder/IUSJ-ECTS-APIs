package iusj.ECTS.services;

import iusj.ECTS.enumerations.ClassLevel;
import iusj.ECTS.enumerations.FileCategory;
import iusj.ECTS.enumerations.Semester;
import iusj.ECTS.models.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    File UploadPv(MultipartFile excelFile, File file);
    List<File> AllPvs(FileCategory category, ClassLevel classLevel, Semester semester, String academicYear);
    void deletePv(Long id);
    File updatePv(MultipartFile excelFile, File file);
}
