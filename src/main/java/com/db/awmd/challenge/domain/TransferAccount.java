package com.db.awmd.challenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TransferAccount {
    @NotNull
    @NotEmpty
    private final String debitAccount;

    @NotNull
    @NotEmpty
    private final String creditAccount;

    @NotNull
    @Min(value = 0, message = "Initial balance must be positive.")
    private final BigDecimal amount;

    @JsonCreator
    public TransferAccount(@JsonProperty("debitAccount") String debitAccount,
                           @JsonProperty("creditAccount") String creditAccount,
                           @JsonProperty("amount") BigDecimal amount) {
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.amount = amount;
    }


    public String getDebitAccount() {
        return debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

}
