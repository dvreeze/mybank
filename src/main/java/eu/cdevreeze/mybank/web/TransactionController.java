package eu.cdevreeze.mybank.web;

import eu.cdevreeze.mybank.dto.TransactionDto;
import eu.cdevreeze.mybank.model.Transaction;
import eu.cdevreeze.mybank.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping(value = "/transactions", produces = "application/json")
    public List<Transaction> findAll() {
        return transactionService.findAll();
    }

    @GetMapping(value = "/transactions", params = "reference", produces = "application/json")
    public List<Transaction> findByReference(
            @RequestParam("reference") @NotBlank String reference
    ) {
        return transactionService.findByReference(reference);
    }

    @GetMapping(value = "/transactions", params = "id", produces = "application/json")
    public Optional<Transaction> findById(
            @RequestParam("id") @NotBlank String id
    ) {
        return transactionService.findById(id);
    }

    @PostMapping(value = "/transactions", consumes = "application/json", produces = "application/json")
    public Transaction create(
            @RequestBody @Valid TransactionDto transactionDto
    ) {
        return transactionService.create(transactionDto.getAmount(), Instant.now(), transactionDto.getReference());
    }
}
