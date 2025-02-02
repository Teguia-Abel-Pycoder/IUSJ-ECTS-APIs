package iusj.ECTS.services.implementation;

import iusj.ECTS.models.AcademicLevel;
import iusj.ECTS.models.Equivalence;
import iusj.ECTS.repositories.EquivalenceRepository;
import iusj.ECTS.services.EquivalenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Override
    public ResponseEntity<String> addCourse(Long id, String type, Map<String, List<String>> newCourses) {
        Optional<Equivalence> optionalEquivalence = equivalenceRepository.findById(id);
        if (optionalEquivalence.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Equivalence not found");
        }

        Equivalence equivalence = optionalEquivalence.get();
        Map<String, List<String>> coursesMap = "isi".equalsIgnoreCase(type)
                ? equivalence.getIsiCourses()
                : "srt".equalsIgnoreCase(type) ? equivalence.getSrtCourses() : null;

        if (coursesMap == null) {
            return ResponseEntity.badRequest().body("Invalid course type. Use 'isi' or 'srt'");
        }

        newCourses.forEach((key, value) ->
                coursesMap.merge(key, value, (existing, newVals) -> {
                    existing.addAll(newVals);
                    return existing;
                })
        );

        try {
            equivalence.serializeMaps(); // Update JSON fields
            equivalenceRepository.save(equivalence);
            return ResponseEntity.ok("Courses added successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request");
        }
    }
    @Override
    public ResponseEntity<String> removeCourse(Long id, String type, Map<String, List<String>> coursesToRemove) {
        Optional<Equivalence> optionalEquivalence = equivalenceRepository.findById(id);
        if (optionalEquivalence.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Equivalence not found");
        }

        Equivalence equivalence = optionalEquivalence.get();
        Map<String, List<String>> coursesMap = "isi".equalsIgnoreCase(type)
                ? equivalence.getIsiCourses()
                : "srt".equalsIgnoreCase(type) ? equivalence.getSrtCourses() : null;

        if (coursesMap == null) {
            return ResponseEntity.badRequest().body("Invalid course type. Use 'isi' or 'srt'");
        }

        coursesToRemove.forEach((key, value) -> {
            if (coursesMap.containsKey(key)) {
                coursesMap.get(key).removeAll(value);
                if (coursesMap.get(key).isEmpty()) {
                    coursesMap.remove(key); // Remove empty keys
                }
            }
        });

        try {
            equivalence.serializeMaps(); // Update JSON fields
            equivalenceRepository.save(equivalence);
            return ResponseEntity.ok("Courses removed successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request");
        }
    }
    public ResponseEntity<String> updateSchoolName(Long id, String newSchoolName) {
        Optional<Equivalence> optionalEquivalence = equivalenceRepository.findById(id);
        if (optionalEquivalence.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Equivalence not found");
        }

        Equivalence equivalence = optionalEquivalence.get();
        equivalence.setSchoolName(newSchoolName);

        equivalenceRepository.save(equivalence);
        return ResponseEntity.ok("School name updated successfully");
    }
}
