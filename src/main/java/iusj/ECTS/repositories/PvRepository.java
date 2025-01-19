package iusj.ECTS.repositories;

import iusj.ECTS.enumerations.ClassLevel;
import iusj.ECTS.enumerations.Semester;
import iusj.ECTS.models.Pv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PvRepository extends JpaRepository<Pv, Long> {
    Optional<Pv> findPvByAcademicYearAndClassLevelAndSemester(String academicYear, ClassLevel classLevel, Semester semester);
}
