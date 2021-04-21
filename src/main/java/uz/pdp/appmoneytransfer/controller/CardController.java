package uz.pdp.appmoneytransfer.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appmoneytransfer.entity.Card;
import uz.pdp.appmoneytransfer.payload.ApiResponse;
import uz.pdp.appmoneytransfer.payload.CardDto;
import uz.pdp.appmoneytransfer.service.CardService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/card")
public class CardController {

    final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public HttpEntity<?> getCard() {
        List<Card> cardList = cardService.get();
        return ResponseEntity.ok(cardList);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        Card card = cardService.getById(id);
        return ResponseEntity.status(card != null ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(card);

    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteById(@PathVariable UUID id) {
        ApiResponse apiResponse = cardService.delete(id);
        return ResponseEntity.status(apiResponse.getSuccess() ? 200 : 404).body(apiResponse);
    }

    @PostMapping
    public HttpEntity<?> addCard(@Valid @RequestBody CardDto cardDto) {

        ApiResponse apiResponse = cardService.add(cardDto);
        return ResponseEntity.status(apiResponse.getSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editById(@PathVariable UUID id, @RequestBody CardDto cardDto) {
        ApiResponse apiResponse = cardService.edit(id, cardDto);
        return ResponseEntity.status(apiResponse.getSuccess() ? 202 : 409).body(apiResponse);

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
