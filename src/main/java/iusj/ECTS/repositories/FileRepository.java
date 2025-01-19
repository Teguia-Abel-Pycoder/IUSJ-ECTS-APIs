package iusj.ECTS.repositories;

import iusj.ECTS.enumerations.ClassLevel;
import iusj.ECTS.enumerations.FileCategory;
import iusj.ECTS.enumerations.Semester;
import iusj.ECTS.models.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findPvByAcademicYearAndClassLevelAndSemesterAndCategory(String academicYear, ClassLevel classLevel, Semester semester,FileCategory category);
    List<File> findByCategory(FileCategory category);
    List<File> findByClassLevel(ClassLevel classLevel);

    List<File> findBySemester(Semester semester);

    List<File> findByAcademicYear(String academicYear);
    List<File> findByCategoryAndClassLevelAndSemesterAndAcademicYear(FileCategory category, ClassLevel classLevel, Semester semester, String academicYear);
}
