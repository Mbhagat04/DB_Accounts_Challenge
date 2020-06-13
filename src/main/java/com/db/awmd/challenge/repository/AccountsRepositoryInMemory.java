package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.InsufficientBalanceException;
import com.db.awmd.challenge.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Repository
public class AccountsRepositoryInMemory implements AccountsRepository, NotificationService {

    public static Logger log = LoggerFactory.getLogger(AccountsRepositoryInMemory.class);
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public void createAccount(Account account) throws DuplicateAccountIdException {
        Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
        if (previousAccount != null) {
            throw new DuplicateAccountIdException(
                    "Account id " + account.getAccountId() + " already exists!");
        }
    }

    @Override
    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    @Override
    public void clearAccounts() {
        accounts.clear();
    }

    @Override
    public synchronized String transferAccount(String fromAccount, String toAccount, BigDecimal amount) throws InsufficientBalanceException {

        Account debitAccount = this.getAccount(fromAccount);
        Account creditAccount = this.getAccount(toAccount);

        log.info(String.format("Initiating Transfer from %s to %s of amount %s", fromAccount, toAccount, amount.toString()));

        Thread Transfer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    creditAccount.depositAmount(debitAccount, amount);
                } catch (InsufficientBalanceException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Transfer.start();
        notifyAboutTransfer(debitAccount,"DEBIT");
        notifyAboutTransfer(creditAccount,"CREDIT");

        log.info("Money Transfer Process completed Successfully");
        return String.format("Money Transfer Successful");
    }


    private Long generateUniqueTransactionId() {
        long val;
        do {
            final UUID uid = UUID.randomUUID();
            final ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
            buffer.putLong(uid.getLeastSignificantBits());
            buffer.putLong(uid.getMostSignificantBits());
            final BigInteger bi = new BigInteger(buffer.array());
            val = bi.longValue();
        }
        // making sure that the ID is in positive space, if its not simply repeat the process
        while (val < 0);
        return val;
    }

    @Override
    public void notifyAboutTransfer(Account account, String transferDescription) {
        log.info(String.format("TransferDescription for %s : %s", account, transferDescription));
    }
}
