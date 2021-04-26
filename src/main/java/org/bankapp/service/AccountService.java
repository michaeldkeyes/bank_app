package org.bankapp.service;

import org.bankapp.exception.BusinessException;
import org.bankapp.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    public Account createAccount(Account account) throws BusinessException;
    public List<Account> getAccountsByOwnerId(int ownerId) throws BusinessException;
    public void updateBalance(Account account, BigDecimal amount) throws BusinessException;
    public Account getAccountById(int accountId) throws BusinessException;
    void transfer(Account fromAccount, Account toAccount, BigDecimal amount) throws BusinessException;
}
