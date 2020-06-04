package com.db.awmd.challenge.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Transaction implements Serializable {

    private static final long serialVersionUID = 6435157498901133988L;

    @NotNull
    @NotEmpty
    private Long transactionId;

    @NotNull
    @NotEmpty
    private String transactionType;

    @NotNull
    @NotEmpty
    private BigDecimal transactionAmount;

    @NotNull
    @NotEmpty
    private Date transactionTimestamp;


    public Transaction() {
        }

    public Long getTransactionId() {
        return transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public Date getTransactionTimestamp() {
        return transactionTimestamp;
    }

    public void setTransactionTimestamp() {
        this.transactionTimestamp = Timestamp.valueOf(LocalDateTime.now());
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public void setTransactionTimestamp(Date transactionTimestamp) {
        this.transactionTimestamp = transactionTimestamp;
    }

}
