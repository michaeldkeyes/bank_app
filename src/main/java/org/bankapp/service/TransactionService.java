package org.bankapp.service;

import org.bankapp.exception.BusinessException;
import org.bankapp.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    public Transaction createTransaction(int fromAccount, int toAccount, String memo, BigDecimal amount) throws BusinessException;
    public List<Transaction> getAllTransactionsByAccount(int accountId) throws BusinessException;
}
