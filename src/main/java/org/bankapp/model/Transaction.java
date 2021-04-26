package org.bankapp.model;

import java.sql.Timestamp;

public class Transaction {
    private int transactionId;
    private int amount;
    private String memo;
    private Timestamp timestamp;
    private int fromAccount;
    private int toAccount;

    public Transaction() {
    }

    public Transaction(int amount, String memo, Timestamp timestamp, int fromAccount, int toAccount) {
        this.amount = amount;
        this.memo = memo;
        this.timestamp = timestamp;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    public Transaction(int transactionId, int amount, String memo, Timestamp timestamp, int fromAccount, int toAccount) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.memo = memo;
        this.timestamp = timestamp;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(int fromAccount) {
        this.fromAccount = fromAccount;
    }

    public int getToAccount() {
        return toAccount;
    }

    public void setToAccount(int toAccount) {
        this.toAccount = toAccount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", amount=" + amount +
                ", memo='" + memo + '\'' +
                ", timestamp=" + timestamp +
                ", fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                '}';
    }
}
