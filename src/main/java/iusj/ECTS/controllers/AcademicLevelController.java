package iusj.ECTS.controllers;

import iusj.ECTS.DTOs.AcademicLevelRequest;
import iusj.ECTS.models.AcademicLevel;
import iusj.ECTS.services.AcademicLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/iusj-ects/api/academic-level")
@CrossOrigin("*")
public class AcademicLevelController {
    @Autowired
    private AcademicLevelService academicLevelService;

    @PostMapping("/create")
    public ResponseEntity<AcademicLevel> createAcademicLevel(@RequestBody AcademicLevelRequest request) {
        AcademicLevel academicLevel = academicLevelService.createAcademicLevel(request.getLevelName(), request.getCourses());
        return new ResponseEntity<>(academicLevel, HttpStatus.CREATED);
    }
    // Endpoint to add multiple courses to an existing academic levl
    @PutMapping("/{academicLevelId}/courses")
    public ResponseEntity<AcademicLevel> addCoursesToAcademicLevel(
            @PathVariable Long academicLevelId,
            @RequestBody Map<String, String> courses) {

        AcademicLevel updatedAcademicLevel = academicLevelService.addCoursesToAcademicLevel(academicLevelId, courses);

        return ResponseEntity.ok(updatedAcademicLevel);
    }
    @DeleteMapping("/{academicLevelId}/courses/{courseName}")
    public ResponseEntity<AcademicLevel> deleteCourseFromAcademicLevel(
            @PathVariable Long academicLevelId,
            @PathVariable String courseName) {

        AcademicLevel updatedAcademicLevel = academicLevelService.deleteCourseFromAcademicLevel(academicLevelId, courseName);

        return ResponseEntity.ok(updatedAcademicLevel);
    }


    // Endpoint  a course in an existing academic level
    @PutMapping("/{academicLevelId}/courses/{courseName}")
    public ResponseEntity<AcademicLevel> modifyCourseInAcademicLevel(
            @PathVariable Long academicLevelId,
            @PathVariable String courseName,
            @RequestParam String newCourseDescription) {

        AcademicLevel updatedAcademicLevel = academicLevelService.modifyCourseInAcademicLevel(academicLevelId, courseName, newCourseDescription);

        return ResponseEntity.ok(updatedAcademicLevel);
    }
}
