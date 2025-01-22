package iusj.ECTS.services;

import iusj.ECTS.models.Equivalence;
import iusj.ECTS.repositories.EquivalenceRepository;

import java.util.List;

public interface EquivalenceService {
    Equivalence createEquivalence(Equivalence equivalence);
    List<Equivalence> getAllEquivalences();
    Equivalence getEquivalenceById(Long id);
}
