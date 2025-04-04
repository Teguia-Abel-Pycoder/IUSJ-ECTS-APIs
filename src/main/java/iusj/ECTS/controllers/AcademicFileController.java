package iusj.ECTS.controllers;

import iusj.ECTS.enumerations.ClassLevel;
import iusj.ECTS.enumerations.FileCategory;
import iusj.ECTS.enumerations.Semester;
import iusj.ECTS.models.AcademicFile;
import iusj.ECTS.services.EquivalenceService;
import iusj.ECTS.services.ExcelHandler;
import iusj.ECTS.services.FileHandler;
import iusj.ECTS.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/iusj-ects/api/file")
@CrossOrigin("*")
public class AcademicFileController {
    @Autowired
    private ExcelHandler excelHandler;

    @Autowired
    private FileHandler fileHandler;

    @Autowired
    private FileService fileService;
    @Autowired
    private EquivalenceService equivalenceService;

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        return fileHandler.handleFileUpload(file);
    }

    public void exampleUsage() {
        excelHandler.readAndManipulateExcel("example.xlsx", ClassLevel.SRT4, true);
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public Resource serveFile(@PathVariable String filename) {
        try {
            Path file = Paths.get(fileHandler.uploadDir).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not serve file: " + filename, e);
        }
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AcademicFile createPv(
            @RequestPart("file") MultipartFile file,
            @RequestPart("fileDetails") AcademicFile academicFileJson) {
        try {
            return fileService.UploadPv(file, academicFileJson);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    @GetMapping("/all")
    public List<AcademicFile> getAllPvs(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) ClassLevel classLevel,
            @RequestParam(required = false) Semester semester,
            @RequestParam(required = false) String academicYear) {

        // Convert the category String to FileCategory enum
        FileCategory fileCategory = null;
        Map<String, String> studentGrades = new HashMap<>();
        studentGrades.put("Ingénierie financière de l'entreprise", "E");
        studentGrades.put("Anglais - Niveau pratique B2", "D");
        studentGrades.put("Systèmes sans fil", "D");
        studentGrades.put("Services Réseaux", "D");
        studentGrades.put("Introduction à l'internet des objets", "E");
        studentGrades.put("S'initier à la Sécurité des Systèmes d'Information", "D");

        String schoolName = "UTT";
        String classType = "srt";
//        double studentMgp = 3.55;
//        Map<String, String> translatedCourses = equivalenceService.convertEquivalences(studentGrades, schoolName, classType);
//        excelHandler.mainFunction( ClassLevel.SRT4, Semester.SEMESTER1, true,  translatedCourses, studentMgp);
        if (category != null && !category.isEmpty()) {
            try {
                fileCategory = FileCategory.valueOf(category); // Convert to enum
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid category provided.");
            }
        }

        // Call the service with the filtering parameters
        return fileService.AllPvs(fileCategory, classLevel, semester, academicYear);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePv(@PathVariable Long id) {
        try {
            fileService.deletePv(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/update/{pvId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AcademicFile updatePv(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("fileDetails") AcademicFile academicFileJson,
            @PathVariable Long pvId) {
        try {
            return fileService.updatePv(file, academicFileJson, pvId);
        } catch (Exception e) {
            throw new RuntimeException("ERROR: " + e.getMessage(), e);
        }
    }

}
