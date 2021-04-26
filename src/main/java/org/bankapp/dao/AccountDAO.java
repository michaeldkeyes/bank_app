package org.bankapp.dao;

import org.bankapp.exception.BusinessException;
import org.bankapp.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDAO {
    public Account createAccount(Account account) throws BusinessException;
    public List<Account> getAccountsByOwnerId(int ownerId) throws BusinessException;
    public void updateBalance(Account account, BigDecimal newBalance) throws BusinessException;
    public Account getAccountById(int accountId) throws BusinessException;
}
