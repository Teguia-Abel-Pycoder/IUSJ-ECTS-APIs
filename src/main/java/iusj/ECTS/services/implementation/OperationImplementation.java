package iusj.ECTS.services.implementation;

import iusj.ECTS.enumerations.OperationStatus;
import iusj.ECTS.models.Operation;
import iusj.ECTS.repositories.OperationRepository;
import iusj.ECTS.services.EquivalenceService;
import iusj.ECTS.services.ExcelHandler;
import iusj.ECTS.services.OperationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        if(operation.getResult() != null){
            operation.setStatus(OperationStatus.Done);
            operation.setComputed(true);
        }else {
            operation.setStatus(OperationStatus.Pending);
            operation.setComputed(false);
        }
        return operationRepository.save(operation);
    }

    @Override
    public Operation saveOperation(Operation operation) {
        operation.setStudentMgp(excelHandler.readAndManipulateExcel1(excelHandler.pvForMgp(operation.getClassLevel(), operation.getSemester()), operation.getClassLevel(),true, operation.getStudentName()));
        return operationRepository.save(operation);
    }

    @Override
    public List<Operation> getAllOperation() {
        return operationRepository.findAll();
    }

    @Override
    public Operation computeOperation(Long id, Operation operationResult) {
        Optional<Operation> optionalOperation = operationRepository.findById(id);
        if (optionalOperation.isEmpty()) {
            return operationRepository.findById(id).orElseThrow(() ->
                    new EntityNotFoundException("Operation not found with id: " + id));
        }
        Operation operation = optionalOperation.get();
        String classType = "";
        if (operation.getClassLevel().toString().startsWith("SRT")) {
            classType = "srt";
        } else if (operation.getClassLevel().toString().startsWith("ISI")) {
            classType = "isi";
        }
        Map<String, String> translatedCourses = equivalenceService.convertEquivalences(operationResult.getGrades(), operation.getSchoolName(), operation.getClassLevel() , classType);
        Map<String, Double> result = excelHandler.mainFunction(operation.getClassLevel(), operation.getSemester(), true, translatedCourses, operation.getStudentMgp());
        if(operation.getResult() != null){
            operation.setStatus(OperationStatus.Done);
            operation.setComputed(true);
        }else {
            operation.setStatus(OperationStatus.Pending);
            operation.setComputed(false);
        }
        return operationRepository.save(operation);

    }


}
