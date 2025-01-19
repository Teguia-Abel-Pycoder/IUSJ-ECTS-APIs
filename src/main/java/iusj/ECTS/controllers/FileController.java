package iusj.ECTS.controllers;

import iusj.ECTS.enumerations.ClassLevel;
import iusj.ECTS.enumerations.FileCategory;
import iusj.ECTS.enumerations.Semester;
import iusj.ECTS.models.File;
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
import java.util.List;

@RestController
@RequestMapping("/iusj-ects/api/file")
@CrossOrigin("*")
public class FileController {

    @Autowired
    private FileHandler fileHandler;

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        return fileHandler.handleFileUpload(file);
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
    public File createPv(
            @RequestPart("file") MultipartFile file,
            @RequestPart("fileDetails") File fileJson) {
        try {
            return fileService.UploadPv(file, fileJson);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    @GetMapping("/all")
    public List<File> getAllPvs(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) ClassLevel classLevel,
            @RequestParam(required = false) Semester semester,
            @RequestParam(required = false) String academicYear) {

        // Convert the category String to FileCategory enum
        FileCategory fileCategory = null;
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
    public File updatePv(
            @RequestPart("file") MultipartFile file,
            @RequestPart("fileDetails") File fileJson) {
        try {
            return fileService.updatePv(file, fileJson);
        } catch (Exception e) {
            throw new RuntimeException("ERROR: " + e.getMessage(), e);
        }
    }
}
