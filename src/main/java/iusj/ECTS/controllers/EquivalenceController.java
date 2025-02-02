package iusj.ECTS.controllers;

import iusj.ECTS.models.Equivalence;
import iusj.ECTS.repositories.EquivalenceRepository;
import iusj.ECTS.services.EquivalenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/iusj-ects/api/equivalence")
@CrossOrigin("*")
public class EquivalenceController {
    @Autowired private EquivalenceService equivalenceService;
    @Autowired private EquivalenceRepository equivalenceRepository;
    @PostMapping("/create")
    public ResponseEntity<Equivalence> createEquivalence(@RequestBody Equivalence equivalence) {
        Equivalence createdEquivalence = equivalenceService.createEquivalence(equivalence);
        return ResponseEntity.ok(createdEquivalence);
    }

    @GetMapping("/all-equivalence")
    public ResponseEntity<List<Equivalence>> getAllEquivalences() {
        return ResponseEntity.ok(equivalenceService.getAllEquivalences());
    }

    @GetMapping("/get-equivalence/{id}")
    public ResponseEntity<Equivalence> getEquivalenceById(@PathVariable Long id) {
        return ResponseEntity.ok(equivalenceService.getEquivalenceById(id));
    }


    @PostMapping("/{id}/add-course")
    public ResponseEntity<String> addCourse(
            @PathVariable Long id,
            @RequestParam("type") String type,
            @RequestBody Map<String, List<String>> newCourses) {
        return equivalenceService.addCourse(id, type, newCourses);
    }
    @PostMapping("/{id}/remove-course")
    public ResponseEntity<String> removeCourse(
            @PathVariable Long id,
            @RequestParam("type") String type,
            @RequestBody Map<String, List<String>> coursesToRemove) {
        return equivalenceService.removeCourse(id, type, coursesToRemove);
    }
}
