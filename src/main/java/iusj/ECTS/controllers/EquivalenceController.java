package iusj.ECTS.controllers;

import iusj.ECTS.models.Equivalence;
import iusj.ECTS.services.EquivalenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/iusj-ects/api/equivalence")
@CrossOrigin("*")
public class EquivalenceController {
    @Autowired private EquivalenceService equivalenceService;
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
}
