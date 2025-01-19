package iusj.ECTS.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import iusj.ECTS.models.Pv;
import iusj.ECTS.services.PvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/iusj-ects/api/pv")
@CrossOrigin("*")
public class PvController {
    @Autowired private PvService pvService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Pv createPv(
            @RequestPart("file") MultipartFile file,
            @RequestPart("pv") Pv pvJson) {
        try {
            // Validate file
            if (file.isEmpty()) {
                throw new RuntimeException("Uploaded file is empty.");
            }

            String contentType = file.getContentType();
            if (!"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType) &&
                    !"application/vnd.ms-excel".equals(contentType)) {
                throw new RuntimeException("Invalid file type. Please upload an Excel file.");
            }

            return pvService.UploadPv(file, pvJson);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload Excel file: " + e.getMessage(), e);
        }
    }
    @GetMapping("/all")
    public List<Pv> getAllPvs() {
        return pvService.AllPvs();
    }
}
