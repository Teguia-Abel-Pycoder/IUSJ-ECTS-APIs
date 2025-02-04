package iusj.ECTS.services.implementation;

import iusj.ECTS.models.Operation;
import iusj.ECTS.repositories.OperationRepository;
import iusj.ECTS.services.ExcelHandler;
import iusj.ECTS.services.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OperationImplementation implements OperationService {
    @Autowired
    private OperationRepository operationRepository;
    @Autowired private ExcelHandler excelHandler;
    public Operation createOperation(Operation operation) {
        Map<String, Double> result = excelHandler.mainFunction(operation.getClassLevel(), operation.getSemester(), true, operation.getGrades(), operation.getStudentMgp());
        operation.setResult(result);
        return operationRepository.save(operation);
    }
}
