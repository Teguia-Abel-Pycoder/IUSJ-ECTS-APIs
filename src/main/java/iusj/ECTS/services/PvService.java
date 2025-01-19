package iusj.ECTS.services;

import iusj.ECTS.models.Pv;
import org.springframework.web.multipart.MultipartFile;

public interface PvService {
    Pv UploadPv(MultipartFile excelFile, Pv pv);
}
