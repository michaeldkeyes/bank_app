package org.bankapp.service.impl;

import org.bankapp.dao.AccountDAO;
import org.bankapp.dao.impl.AccountDAOImpl;
import org.bankapp.exception.BusinessException;
import org.bankapp.model.Account;
import org.bankapp.service.AccountService;

import java.math.BigDecimal;
import java.util.List;

public class AccountServiceImpl implements AccountService {
    private final AccountDAO accountDAO = new AccountDAOImpl();

    @Override
    public Account createAccount(Account account) throws BusinessException {
        return accountDAO.createAccount(account);
    }

    @Override
    public List<Account> getAccountsByOwnerId(int ownerId) throws BusinessException {
        return accountDAO.getAccountsByOwnerId(ownerId);
    }

    @Override
    public Account updateBalance(int accountId, BigDecimal newBalance) throws BusinessException {
        return accountDAO.updateBalance(accountId, newBalance);
    }


}
