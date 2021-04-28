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
    public List<Account> getAccountsByPending(boolean isPending) throws BusinessException;
    public void transfer(Account fromAccount, Account toAccount, BigDecimal amount) throws BusinessException;
    public void updatePending(Account account, boolean pending) throws BusinessException;
}
