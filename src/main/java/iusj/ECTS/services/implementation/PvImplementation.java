package iusj.ECTS.services.implementation;

import iusj.ECTS.controllers.FileController;
import iusj.ECTS.models.Pv;
import iusj.ECTS.repositories.PvRepository;
import iusj.ECTS.services.PvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PvImplementation implements PvService {
    @Autowired
    private FileController fileController;
    @Autowired
    private PvRepository pvRepository;

    @Override
    public Pv UploadPv(MultipartFile excelFile, Pv pv) {
        pv.setPvPath(fileController.handleFileUpload(excelFile));
        System.out.println("PV: \n\t"+ pv.toString());
        return pvRepository.save(pv);
    }

    @Override
    public List<Pv> AllPvs() {
        return pvRepository.findAll();
    }
}
