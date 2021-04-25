package org.bankapp.model;

import java.util.Date;
import java.util.List;

public class Account {
    private int accountId;
    private String type;
    private double balance;
    private int ownerId;
    private boolean pending;
    private Date createdAt;
    private List<Transaction> transactions;

    public Account() {
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Account(String type, int ownerId) {
        this.type = type;
        this.balance = 0;
        this.ownerId = ownerId;
        this.pending = true;
    }

    public Account(int accountId, String type, int ownerId) {
        this.accountId = accountId;
        this.type = type;
        this.balance = 0;
        this.ownerId = ownerId;
        this.pending = true;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", type='" + type + '\'' +
                ", balance=" + balance +
                ", ownerId=" + ownerId +
                ", pending=" + pending +
                ", createdAt=" + createdAt +
                ", transactions=" + transactions +
                '}';
    }
}
