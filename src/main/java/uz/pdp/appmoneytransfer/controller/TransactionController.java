package uz.pdp.appmoneytransfer.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appmoneytransfer.entity.Transaction;
import uz.pdp.appmoneytransfer.payload.ApiResponse;
import uz.pdp.appmoneytransfer.payload.CardInfo;
import uz.pdp.appmoneytransfer.payload.TransactionDto;
import uz.pdp.appmoneytransfer.service.TransactionService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping()
    public HttpEntity<?> transaction(@Valid @RequestBody TransactionDto transactionDto) {
        ApiResponse apiResponse = transactionService.transaction(transactionDto);
        return ResponseEntity.status(apiResponse.getSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getAllTransaction() {
        List<Transaction> transactionList = transactionService.getAll();
        return ResponseEntity.ok(transactionList);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        Transaction transaction = transactionService.getById(id);
        return ResponseEntity.status(transaction != null ? 200 : 409).body(transaction);
    }

    @GetMapping("/infoIncome")
    public HttpEntity<?> getInfoIncome(@RequestParam UUID cardId) {
        List<CardInfo> infoIncome = transactionService.getInfoIncome(cardId);
        return ResponseEntity.ok(infoIncome);
    }

    @GetMapping("/infoOutcome")
    public HttpEntity<?> getInfoOutcome(@RequestParam UUID cardId) {
        List<CardInfo> getAll = transactionService.getInfoOutcome(cardId);
        return ResponseEntity.ok(getAll);
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
