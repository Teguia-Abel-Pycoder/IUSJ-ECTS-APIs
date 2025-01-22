package iusj.ECTS.repositories;

import iusj.ECTS.enumerations.ClassLevel;
import iusj.ECTS.enumerations.FileCategory;
import iusj.ECTS.enumerations.Semester;
import iusj.ECTS.models.AcademicFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcademicFileRepository extends JpaRepository<AcademicFile, Long> {
    Optional<AcademicFile> findPvByAcademicYearAndClassLevelAndSemesterAndCategory(String academicYear, ClassLevel classLevel, Semester semester, FileCategory category);
    List<AcademicFile> findByCategory(FileCategory category);
    List<AcademicFile> findByClassLevel(ClassLevel classLevel);
    AcademicFile findByCategoryAndAcademicYearAndClassLevelAndSemester(FileCategory category, String academicYear, ClassLevel classLevel, Semester semester);
    List<AcademicFile> findBySemester(Semester semester);

    List<AcademicFile> findByAcademicYear(String academicYear);
    List<AcademicFile> findByCategoryAndClassLevelAndSemesterAndAcademicYear(FileCategory category, ClassLevel classLevel, Semester semester, String academicYear);
}
