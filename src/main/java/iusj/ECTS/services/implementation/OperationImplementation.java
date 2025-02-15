package iusj.ECTS.services.implementation;

import iusj.ECTS.models.Operation;
import iusj.ECTS.repositories.OperationRepository;
import iusj.ECTS.services.EquivalenceService;
import iusj.ECTS.services.ExcelHandler;
import iusj.ECTS.services.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OperationImplementation implements OperationService {
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private EquivalenceService equivalenceService;
    @Autowired private ExcelHandler excelHandler;
    public Operation createOperation(Operation operation) {

        String classType = "";

        if (operation.getClassLevel().toString().startsWith("SRT")) {
            classType = "srt";
        } else if (operation.getClassLevel().toString().startsWith("ISI")) {
            classType = "isi";
        }

        Map<String, String> translatedCourses = equivalenceService.convertEquivalences(operation.getGrades(), operation.getSchoolName(), operation.getClassLevel() , classType);
        operation.setStudentMgp(excelHandler.readAndManipulateExcel1(excelHandler.pvForMgp(operation.getClassLevel(), operation.getSemester()), operation.getClassLevel(),true, operation.getStudentName()));
        Map<String, Double> result = excelHandler.mainFunction(operation.getClassLevel(), operation.getSemester(), true, translatedCourses, operation.getStudentMgp());
        operation.setResult(result);
        return operationRepository.save(operation);
    }
}
