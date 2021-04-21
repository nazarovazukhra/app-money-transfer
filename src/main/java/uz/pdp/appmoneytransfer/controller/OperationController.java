package uz.pdp.appmoneytransfer.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appmoneytransfer.entity.Operation;
import uz.pdp.appmoneytransfer.payload.ApiResponse;
import uz.pdp.appmoneytransfer.service.OperationService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/operation")
public class OperationController {

    final OperationService operationService;

    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    @PostMapping
    public HttpEntity<?> addOperation(@Valid @RequestBody Operation operation) {
        ApiResponse apiResponse = operationService.add(operation);
        return ResponseEntity.status(apiResponse.getSuccess() ? 201 : 409).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getAll() {
        List<Operation> operations = operationService.getAll();
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable Integer id) {
        Operation operation = operationService.getById(id);
        return ResponseEntity.status(operation != null ? 200 : 404).body(operation);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteById(@PathVariable Integer id) {
        ApiResponse apiResponse = operationService.delete(id);
        return ResponseEntity.status(apiResponse.getSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editById(@PathVariable Integer id, @RequestBody Operation operation) {
        ApiResponse apiResponse = operationService.edit(id, operation);
        return ResponseEntity.status(apiResponse.getSuccess() ? HttpStatus.ACCEPTED : HttpStatus.NOT_FOUND).body(apiResponse);

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
