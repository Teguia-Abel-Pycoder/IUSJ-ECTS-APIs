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
        System.out.println("=======================================================================");
        System.out.println("=======================================================================");
        System.out.println("=======================================================================");
        System.out.println("=======================================================================");
        System.out.println("The GRADES"+ operation.getGrades());
        Map<String, String> translatedCourses = equivalenceService.convertEquivalences(operation.getGrades(), schoolName, classType);

        Map<String, Double> result = excelHandler.mainFunction(operation.getClassLevel(), operation.getSemester(), true, translatedCourses, operation.getStudentMgp());
        operation.setResult(result);
        return operationRepository.save(operation);
    }
}
