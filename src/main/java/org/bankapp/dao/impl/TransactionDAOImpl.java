package org.bankapp.dao.impl;

import org.bankapp.dao.TransactionDAO;
import org.bankapp.dao.dbutil.PostgresConnection;
import org.bankapp.exception.BusinessException;
import org.bankapp.model.Transaction;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {

    /**
     * Creates a new Transaction in the database
     * @param fromAccount - The account id that the transaction is coming from
     * @param toAccount - The account id that the transaction is going to
     * @param memo - A string detailing what the transaction is for
     * @param amount - The amount of the transaction
     * @return - A new Transaction object
     * @throws BusinessException - If an SQLException occurs or the transaction could not be added to the database
     */
    @Override
    public Transaction createTransaction(int fromAccount, int toAccount, String memo, BigDecimal amount) throws BusinessException{
        try (Connection connection = PostgresConnection.getConnection()) {
            String sql = "INSERT INTO bankapp_schema.transactions (memo, \"timestamp\", from_account_id, to_account_id) VALUES(?, ?, ?, ?);\n";
            try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, memo);
                ps.setTimestamp(2, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
                ps.setInt(3, fromAccount);
                ps.setInt(4, toAccount);

                int c = ps.executeUpdate();
                if (c == 1) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        Transaction transaction = new Transaction();
                        transaction.setTransactionId(rs.getInt("transaction_id"));
                        transaction.setMemo(rs.getString("memo"));
                        transaction.setTimestamp(rs.getTimestamp("timestamp"));
                        transaction.setFromAccount(rs.getInt("from_account_id"));
                        transaction.setToAccount(rs.getInt("to_account_id"));
                        return transaction;
                    } else {
                        throw new BusinessException("Account could not be created.");
                    }
                }
            }
            return null;
        } catch (SQLException e) {
            throw new BusinessException("Internal error occured. Please contact sysadmin " + e);
        }
    }

    @Override
    public List<Transaction> getAllTransactionsByAccount(int accountId) throws BusinessException {

    }
}
