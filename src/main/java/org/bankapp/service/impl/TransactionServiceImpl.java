package org.bankapp.service.impl;

import org.bankapp.dao.TransactionDAO;
import org.bankapp.dao.impl.TransactionDAOImpl;
import org.bankapp.exception.BusinessException;
import org.bankapp.model.Transaction;
import org.bankapp.service.TransactionService;

import java.math.BigDecimal;
import java.util.List;

public class TransactionServiceImpl implements TransactionService {
    private final TransactionDAO transactionDAO = new TransactionDAOImpl();

    @Override
    public void createTransaction(int fromAccount, int toAccount, String memo, BigDecimal amount) throws BusinessException {
        transactionDAO.createTransaction(fromAccount, toAccount, memo, amount);
    }

    @Override
    public List<Transaction> getAllTransactionsByAccount(int accountId) throws BusinessException {
        return transactionDAO.getAllTransactionsByAccount(accountId);
    }
}
