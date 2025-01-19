package iusj.ECTS.controllers;

import iusj.ECTS.models.Pv;
import iusj.ECTS.services.PvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/iusj-ects/api/pv")
@CrossOrigin("*")
public class PvController {
    @Autowired private PvService pvService;
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Pv createPv(@RequestPart("file") MultipartFile file, @RequestPart("pv") Pv pv) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Uploaded file is empty.");
            }

            String contentType = file.getContentType();
            if (!"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType) &&
                    !"application/vnd.ms-excel".equals(contentType)) {
                throw new RuntimeException("Invalid file type. Please upload an Excel file.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload Excel file: " + e.getMessage(), e);
        }
        return pvService.UploadPv(file, pv);
    }

}
