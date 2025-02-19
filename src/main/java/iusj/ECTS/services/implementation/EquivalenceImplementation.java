package iusj.ECTS.services.implementation;

import iusj.ECTS.enumerations.ClassLevel;
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
import java.util.*;

@Service
public class EquivalenceImplementation implements EquivalenceService {
    @Autowired
    private  EquivalenceRepository equivalenceRepository;


    @Override
    public Equivalence createEquivalence(Equivalence equivalence) {
        Optional<Equivalence> optionalEquivalence = equivalenceRepository.findEquivalenceBySchoolNameAndAcademicLevel(equivalence.getSchoolName(), equivalence.getAcademicLevel());

        if (optionalEquivalence.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Equivalence for " + equivalence.getSchoolName() + " " + equivalence.getAcademicLevel() + " already exists!");
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

        List<String> duplicateCourses = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : newCourses.entrySet()) {
            String key = entry.getKey();
            List<String> newVals = new ArrayList<>(entry.getValue()); // Copy to avoid modifying the original list

            if (coursesMap.containsKey(key)) {
                List<String> existing = coursesMap.get(key);

                // Identify duplicates
                List<String> duplicates = newVals.stream()
                        .filter(existing::contains)
                        .toList(); // Find values that already exist

                // Remove duplicates from newVals before adding
                newVals.removeAll(duplicates);

                // Add only non-duplicates
                existing.addAll(newVals);

                // Store duplicate values to return an error
                duplicateCourses.addAll(duplicates);
            } else {
                // If the key doesn't exist, just add it
                coursesMap.put(key, newVals);
            }
        }

        try {
            equivalence.serializeMaps(); // Update JSON fields
            equivalenceRepository.save(equivalence);

            // If there were duplicate values, return an error message while still adding the valid ones
            if (!duplicateCourses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Some courses were added successfully, but the following already exist: " + duplicateCourses);
            }

            return ResponseEntity.ok("Courses added successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request");
        }
    }

    @Override
    public ResponseEntity<String> addEquivalence(String schoolName, ClassLevel academicLevel, String type, Map<String, List<String>> newCourses) {
        // Validate course type early
        if (!"isi".equalsIgnoreCase(type) && !"srt".equalsIgnoreCase(type)) {
            return ResponseEntity.badRequest().body("Invalid course type. Use 'isi' or 'srt'");
        }
        Optional<Equivalence> optionalEquivalence = equivalenceRepository.findEquivalenceBySchoolNameAndAcademicLevel(schoolName, academicLevel);

        if (optionalEquivalence.isPresent()) {
//          If equivalence exists, add courses while checking for duplicates
            Equivalence equivalence = optionalEquivalence.get();
            return addCourse(equivalence.getEquivalenceId(), type, newCourses);
        }

        // Filter duplicates within newCourses before adding
        Map<String, List<String>> filteredCourses = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : newCourses.entrySet()) {
            List<String> uniqueValues = new ArrayList<>(new HashSet<>(entry.getValue())); // Remove duplicates
            filteredCourses.put(entry.getKey(), uniqueValues);
        }

        // Create new equivalence
        Equivalence newEquivalence = new Equivalence();
        newEquivalence.setAcademicLevel(academicLevel);
        newEquivalence.setSchoolName(schoolName);

        // Assign filtered courses
        if ("isi".equalsIgnoreCase(type)) {
            newEquivalence.setIsiCourses(filteredCourses);
        } else {
            newEquivalence.setSrtCourses(filteredCourses);
        }

        try {
            newEquivalence.serializeMaps(); // Update JSON fields
            equivalenceRepository.save(newEquivalence);
            return ResponseEntity.ok("Equivalence created successfully. Duplicate courses were removed.");
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
    public ResponseEntity<String> updateSchoolName(Long id, String newSchoolName, ClassLevel academicLevel) {
        Optional<Equivalence> optionalEquivalence = equivalenceRepository.findById(id);
        if (optionalEquivalence.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Equivalence not found");
        }
        Optional<Equivalence> optionalEquivalence2 = equivalenceRepository.findEquivalenceBySchoolNameAndAcademicLevel(newSchoolName, academicLevel);

        if (optionalEquivalence2.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Equivalence for " + newSchoolName + " already exists!");
        }
        Equivalence equivalence = optionalEquivalence.get();
        equivalence.setSchoolName(newSchoolName);

        equivalenceRepository.save(equivalence);
        return ResponseEntity.ok("School name updated successfully");
    }

    @Override
    public Map<String, String> convertEquivalences(Map<String, String> studentGrades, String schoolName, ClassLevel classLevel, String classType) {
        Optional<Equivalence> optionalEquivalence = equivalenceRepository.findEquivalenceBySchoolNameAndAcademicLevel(schoolName, classLevel);

        if (optionalEquivalence.isEmpty()) {
            System.out.println("==============================================================================================");
            System.out.println("==============================================================================================");
            System.out.println("==============================================================================================");
            System.out.println("==============================================================================================");
            System.out.println("==============================================================================================");
            System.out.println("==============================================================================================");
            System.out.println("==============================================================================================");
            System.out.println("==============================================================================================");
            System.out.println("==============================================================================================");
            System.out.println("==============================================================================================");
            System.out.println("==============================================================================================");
            System.out.println("==============================================================================================");
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
        Map<String, List<String>> newMap = new HashMap<>();

        // Iterate over the original map
        for (Map.Entry<String, List<String>> entry : coursesMap.entrySet()) {
            // Extract string after the first hyphen
            String newKey = "";
            String[] parts = entry.getKey().split("-", 2);
            if (parts.length > 1) {
                newKey = parts[1].trim(); // Extract after the first hyphen and trim any spaces
            }

            // Put the new key with the same value in the new map
            newMap.put(newKey, entry.getValue());
        }
        System.out.println("Translated Courses:===================================================================================================================");
        for (Map.Entry<String, List<String>> entry : newMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
        studentGrades.forEach((key, value) -> {

            List<String> equivalentCourses = newMap.get(key);
            if (equivalentCourses != null) {
                equivalentCourses.forEach(course -> result.put(course, value));
            }
        });

        return result;
    }


}
