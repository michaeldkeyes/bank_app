package org.bankapp.service;

import org.bankapp.exception.BusinessException;
import org.bankapp.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    public Account createAccount(Account account) throws BusinessException;
    public List<Account> getAccountsByOwnerId(int ownerId) throws BusinessException;
    public Account updateBalance(int accountId, BigDecimal newBalance) throws BusinessException;
}
