package iusj.ECTS.services.implementation;

import iusj.ECTS.controllers.FileController;
import iusj.ECTS.models.Pv;
import iusj.ECTS.repositories.PvRepository;
import iusj.ECTS.services.PvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class PvImplementation implements PvService {
    @Autowired
    private FileController fileController;
    @Autowired
    private PvRepository pvRepository;

    @Override
    public Pv UploadPv(MultipartFile excelFile, Pv pv) {
        pv.setPvPath(fileController.handleFileUpload(excelFile));
//        System.out.println("PV: \n\t"+ pv.getUploadedBy());
//        System.out.println("PV: \n\t"+ pv.getAcademicYear());
//        System.out.println("PV: \n\t"+ pv.getPvId());
//        System.out.println("PV: \n\t"+ pv.getSemester());
//        System.out.println("PV: \n\t"+ pv.getClassLevel());
        Optional<Pv> existingPv = pvRepository.findPvByAcademicYearAndClassLevelAndSemester(pv.getAcademicYear(),
                pv.getClassLevel(), pv.getSemester());

        if (existingPv.isPresent()) {
            throw new RuntimeException("A PV with the same class level, semester, and academic year already exists.");
        }
        return pvRepository.save(pv);
    }

    @Override
    public List<Pv> AllPvs() {
        return pvRepository.findAll();
    }

    @Override
    public void deletePv(Long id) {
        pvRepository.deleteById(id);
    }

    @Override
    public Pv updatePv(MultipartFile excelFile, Pv pv) {
        if (excelFile != null && !excelFile.isEmpty()) {
            pv.setPvPath(fileController.handleFileUpload(excelFile));
        }
        Pv existingPv = pvRepository.findById(pv.getPvId()).orElseThrow(() -> new RuntimeException("Crop not found with id: " + pv.getPvId()));
        existingPv.setPvPath(pv.getPvPath());
        existingPv.setClassLevel(pv.getClassLevel());
        existingPv.setSemester(pv.getSemester());
        existingPv.setAcademicYear(pv.getAcademicYear());
        existingPv.setUploadedBy(pv.getUploadedBy());

        return pvRepository.save(existingPv);
    }
}
