package eu.cdevreeze.mybank.service;

import eu.cdevreeze.mybank.model.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class TransactionService {

    private final List<Transaction> transactions = new CopyOnWriteArrayList<>();

    private final String bankSlogan;

    public TransactionService(@Value("${bank.slogan}") String bankSlogan) {
        this.bankSlogan = bankSlogan;
    }

    public Transaction create(BigDecimal amount, Instant timestamp, String reference, String receivingUserId) {
        Transaction transaction = new Transaction(
                UUID.randomUUID().toString(),
                amount,
                timestamp,
                reference,
                bankSlogan,
                receivingUserId
        );
        transactions.add(transaction);
        return transaction;
    }

    public Optional<Transaction> findById(String id) {
        return transactions.stream().filter(t -> t.id().equals(id)).findFirst();
    }

    public List<Transaction> findByReference(String reference) {
        return transactions.stream().filter(t -> t.reference().equalsIgnoreCase(reference)).toList();
    }

    public List<Transaction> findByReceivingUserId(String receivingUserId) {
        return transactions.stream().filter(t -> t.receivingUser().equalsIgnoreCase(receivingUserId)).toList();
    }

    public List<Transaction> findAll() {
        return List.copyOf(transactions);
    }
}
