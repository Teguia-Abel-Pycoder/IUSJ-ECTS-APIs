package iusj.ECTS.services.implementation;

import iusj.ECTS.models.Equivalence;
import iusj.ECTS.repositories.EquivalenceRepository;
import iusj.ECTS.services.EquivalenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EquivalenceImplementation implements EquivalenceService {
    @Autowired
    private  EquivalenceRepository equivalenceRepository;


    @Override
    public Equivalence createEquivalence(Equivalence equivalence) {
        return equivalenceRepository.save(equivalence);
    }

    @Override
    public List<Equivalence> getAllEquivalences() {
        return equivalenceRepository.findAll();
    }

    @Override
    public Equivalence getEquivalenceById(Long id) {
        return equivalenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equivalence not found with ID: " + id));
    }

}
