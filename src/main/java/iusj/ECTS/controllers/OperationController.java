package iusj.ECTS.controllers;

import iusj.ECTS.models.Operation;
import iusj.ECTS.services.ExcelHandler;
import iusj.ECTS.services.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/iusj-ects/api/operation")
@CrossOrigin("*")
public class OperationController {

    @Autowired
    private OperationService operationService;
    @PostMapping("/perform")
    public ResponseEntity<Operation> createOperation(@RequestBody Operation operation) {
        Operation savedOperation = operationService.createOperation(operation);
        return ResponseEntity.ok(savedOperation);
    }
}
