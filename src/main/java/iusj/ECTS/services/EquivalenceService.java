package iusj.ECTS.services;

import iusj.ECTS.enumerations.ClassLevel;
import iusj.ECTS.models.Equivalence;
import iusj.ECTS.repositories.EquivalenceRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface EquivalenceService {
    Equivalence createEquivalence(Equivalence equivalence);
    List<Equivalence> getAllEquivalences();
    Equivalence getEquivalenceById(Long id);
    ResponseEntity<String> addCourse(Long id, String type, Map<String, List<String>> newCourses);
    ResponseEntity<String> addEquivalence(String schoolName, ClassLevel academicLevel, String type, Map<String, List<String>> newCourses);

    ResponseEntity<String> removeCourse(Long id, String type, Map<String, List<String>> coursesToRemove);
    ResponseEntity<String> updateSchoolName(Long id, String newSchoolName, ClassLevel academicLevel);
    Map<String, String> convertEquivalences(Map<String, String> studentGrades, String schoolName, ClassLevel classLevel, String classType);
}
