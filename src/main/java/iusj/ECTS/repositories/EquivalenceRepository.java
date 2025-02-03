package iusj.ECTS.repositories;

import iusj.ECTS.models.Equivalence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquivalenceRepository extends JpaRepository<Equivalence, Long> {
    Optional<Equivalence> findEquivalenceBySchoolName(String schoolName);
}
