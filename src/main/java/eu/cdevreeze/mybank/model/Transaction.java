package eu.cdevreeze.mybank.model;

import java.math.BigDecimal;
import java.time.Instant;

public record Transaction(
        String id,
        BigDecimal amount,
        Instant timestamp,
        String reference,
        String bankSlogan,
        String receivingUser) {
}
