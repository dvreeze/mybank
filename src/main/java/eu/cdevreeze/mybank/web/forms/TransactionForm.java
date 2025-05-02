package eu.cdevreeze.mybank.web.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class TransactionForm {

    @NotBlank
    private String receivingUserId;

    @Positive
    private int amount;

    @NotBlank
    private String reference;

    public String getReceivingUserId() {
        return receivingUserId;
    }

    public void setReceivingUserId(String receivingUserId) {
        this.receivingUserId = receivingUserId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
