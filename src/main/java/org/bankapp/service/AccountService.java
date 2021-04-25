package org.bankapp.service;

import org.bankapp.exception.BusinessException;
import org.bankapp.model.Account;

import java.util.List;

public interface AccountService {
    public Account createAccount(Account account) throws BusinessException;
    public List<Account> getAccountsByOwnerId(int ownerId) throws BusinessException;
}
