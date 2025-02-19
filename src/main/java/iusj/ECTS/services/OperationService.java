package iusj.ECTS.services;

import iusj.ECTS.models.Operation;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface OperationService {
    Operation createOperation(Operation operation);
    Operation saveOperation(Operation operation);
    List<Operation> getAllOperation();
    Operation computeOperation(Long id, Operation operationResult);
}
