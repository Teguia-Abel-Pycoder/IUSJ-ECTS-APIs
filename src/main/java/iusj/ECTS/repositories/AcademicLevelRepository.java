package iusj.ECTS.repositories;

import iusj.ECTS.models.AcademicLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademicLevelRepository extends JpaRepository<AcademicLevel, Long> {
    AcademicLevel findByLevelName(String name);
}