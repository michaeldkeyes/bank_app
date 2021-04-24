package org.bankapp.model;

import java.util.Date;

public class Transaction {
    private int transactionId;
    private int amount;
    private String memo;
    private Date timeStamp;
    private Account account;

    public Transaction() {
    }

    public Transaction(int amount, String memo, Account account) {
        this.amount = amount;
        this.memo = memo;
        this.account = account;
    }

    public Transaction(int transactionId, int amount, String memo, Account account) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.memo = memo;
        this.account = account;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", amount=" + amount +
                ", memo='" + memo + '\'' +
                ", account=" + account +
                '}';
    }
}
