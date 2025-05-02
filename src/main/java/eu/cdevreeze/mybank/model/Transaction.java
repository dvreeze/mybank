package eu.cdevreeze.mybank.model;

import java.time.Instant;

public record Transaction(
        String id,
        int amount,
        Instant timestamp,
        String reference,
        String bankSlogan,
        String receivingUser) {
}
