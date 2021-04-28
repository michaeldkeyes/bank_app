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
    public void updateBalance(Account account, BigDecimal amount) throws BusinessException {
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
        accountDAO.updateBalance(account, newBalance);
    }

    @Override
    public Account getAccountById(int accountId) throws BusinessException {
        return accountDAO.getAccountById(accountId);
    }

    @Override
    public List<Account> getAccountsByPending(boolean isPending) throws BusinessException {
        return accountDAO.getAccountsByPending(isPending);
    }

    @Override
    public void transfer(Account fromAccount, Account toAccount, BigDecimal amount) throws BusinessException {
        updateBalance(fromAccount, amount.negate());
        updateBalance(toAccount, amount);
    }

    @Override
    public void updatePending(Account account, boolean pending) throws BusinessException {
        accountDAO.updatePending(account, pending);
    }

}
