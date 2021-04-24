package org.bankapp.model;

import java.util.List;

public class Account {
    private int accountId;
    private String type;
    private double balance;
    private User owner;
    private List<Transaction> transactions;

    public Account() {
    }

    public Account(String type, double balance, User owner, List<Transaction> transactions) {
        this.type = type;
        this.balance = balance;
        this.owner = owner;
        this.transactions = transactions;
    }

    public Account(int accountId, String type, double balance, User owner, List<Transaction> transactions) {
        this.accountId = accountId;
        this.type = type;
        this.balance = balance;
        this.owner = owner;
        this.transactions = transactions;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
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
                ", owner=" + owner +
                ", transactions=" + transactions +
                '}';
    }
}
