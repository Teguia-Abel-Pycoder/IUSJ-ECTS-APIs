package iusj.ECTS.services.implementation;

import iusj.ECTS.controllers.FileController;
import iusj.ECTS.models.Pv;
import iusj.ECTS.repositories.PvRepository;
import iusj.ECTS.services.PvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PvImplementation implements PvService {
    @Autowired
    private FileController fileController;
    private PvRepository pvRepository;

    @Override
    public Pv UploadPv(MultipartFile excelFile, Pv pv) {
        pv.setPvPath(fileController.handleFileUpload(excelFile));
        pvRepository.save(pv);
        return null;
    }
}
