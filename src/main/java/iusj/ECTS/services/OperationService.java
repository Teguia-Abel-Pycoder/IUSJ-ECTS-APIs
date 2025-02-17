package iusj.ECTS.services;

import iusj.ECTS.models.Operation;

import java.util.List;

public interface OperationService {
    Operation createOperation(Operation operation);
    Operation saveOperation(Operation operation);
    List<Operation> getAllOperation();
}
