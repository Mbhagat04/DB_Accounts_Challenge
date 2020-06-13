package com.db.awmd.challenge.domain;

import com.db.awmd.challenge.exception.InsufficientBalanceException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class Account {

  @NotNull
  @NotEmpty
  private final String accountId;

  @NotNull
  @Min(value = 0, message = "Initial balance must be positive.")
  private BigDecimal balance;

  private final Lock lock = new ReentrantLock();
  private final Random number = new Random(123L);

  public Account(String accountId) {
    this.accountId = accountId;
    this.balance = BigDecimal.ZERO;
  }

  @JsonCreator
  public Account(@JsonProperty("accountId") String accountId,
    @JsonProperty("balance") BigDecimal balance) {
    this.accountId = accountId;
    this.balance = balance;
  }

  public String getAccountId() {
    return accountId;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public synchronized void depositAmount(Account debitAccount, BigDecimal amount) throws InsufficientBalanceException, InterruptedException {
    while(true){
      if(this.lock.tryLock()){
        try {
          if (debitAccount.lock.tryLock()) {
            try {
              if (debitAccount.getBalance().compareTo(amount) < 0) {
                throw new InsufficientBalanceException(" Insufficient Balance");
              }
              debitAccount.getBalance().subtract(amount);
              setBalance(this.balance.add(amount));
              break;
            } finally {
              debitAccount.lock.unlock();
            }
          }
        } finally {
          this.lock.unlock();
        }
        int n = number.nextInt(1000);
        int TIME = 1000 + n; // 1 second + random delay to prevent livelock
        Thread.sleep(TIME);
      }
    }

  }
}
