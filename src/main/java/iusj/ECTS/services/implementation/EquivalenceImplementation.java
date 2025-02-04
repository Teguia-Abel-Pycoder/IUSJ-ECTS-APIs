package iusj.ECTS.services.implementation;

import iusj.ECTS.models.AcademicLevel;
import iusj.ECTS.models.Equivalence;
import iusj.ECTS.repositories.EquivalenceRepository;
import iusj.ECTS.services.EquivalenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EquivalenceImplementation implements EquivalenceService {
    @Autowired
    private  EquivalenceRepository equivalenceRepository;


    @Override
    public Equivalence createEquivalence(Equivalence equivalence) {
        Optional<Equivalence> optionalEquivalence = equivalenceRepository.findEquivalenceBySchoolName(equivalence.getSchoolName());

        if (optionalEquivalence.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Equivalence for " + equivalence.getSchoolName() + " already exists!");
        }
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
        Optional<Equivalence> optionalEquivalence2 = equivalenceRepository.findEquivalenceBySchoolName(newSchoolName);

        if (optionalEquivalence2.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Equivalence for " + newSchoolName + " already exists!");
        }
        Equivalence equivalence = optionalEquivalence.get();
        equivalence.setSchoolName(newSchoolName);

        equivalenceRepository.save(equivalence);
        return ResponseEntity.ok("School name updated successfully");
    }

    @Override
    public Map<String, String> convertEquivalences(Map<String, String> studentGrades, String schoolName, String classType) {
        Optional<Equivalence> optionalEquivalence = equivalenceRepository.findEquivalenceBySchoolName(schoolName);

        if (optionalEquivalence.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Equivalence not found");
        }

        Equivalence equivalence = optionalEquivalence.get();
        Map<String, List<String>> coursesMap;

        if ("isi".equalsIgnoreCase(classType)) {
            coursesMap = equivalence.getIsiCourses();
        } else if ("srt".equalsIgnoreCase(classType)) {
            coursesMap = equivalence.getSrtCourses();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid course type. Use 'isi' or 'srt'");
        }

        if (coursesMap == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Equivalence data is missing for the given class type");
        }

        Map<String, String> result = new HashMap<>();

        studentGrades.forEach((key, value) -> {
            List<String> equivalentCourses = coursesMap.get(key);
            if (equivalentCourses != null) {
                equivalentCourses.forEach(course -> result.put(course, value));
            }
        });

        return result;
    }


}
