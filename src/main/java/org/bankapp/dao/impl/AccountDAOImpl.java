package org.bankapp.dao.impl;

import org.bankapp.dao.AccountDAO;
import org.bankapp.dao.dbutil.PostgresConnection;
import org.bankapp.exception.BusinessException;
import org.bankapp.model.Account;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAOImpl implements AccountDAO {
    @Override
    public Account createAccount(Account account) throws BusinessException{
        try (Connection connection = PostgresConnection.getConnection()) {
            String sql = "INSERT INTO bankapp_schema.accounts (\"type\", balance, owner_id, pending, created_at) VALUES(?, 0, ?, true, ?);\n";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, account.getType());
                preparedStatement.setInt(2, account.getOwnerId());
                preparedStatement.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));

                int c = preparedStatement.executeUpdate();
                if (c == 1) {
                    ResultSet resultSet = preparedStatement.getGeneratedKeys();
                    if (resultSet.next()) {
                        account.setAccountId(resultSet.getInt("account_id"));
                        account.setCreatedAt(resultSet.getDate("created_at"));
                        return account;
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
    public List<Account> getAccountsByOwnerId(int ownerId) throws BusinessException {
        try (Connection connection = PostgresConnection.getConnection()) {
            String sql = "SELECT account_id, \"type\", balance, owner_id, pending, created_at FROM bankapp_schema.accounts where owner_id = ?;\n";

            try(PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, ownerId);

                ResultSet resultSet = preparedStatement.executeQuery();
                List<Account> accounts = new ArrayList<>();
                while (resultSet.next()) {
                    Account account = new Account();
                    account.setAccountId(resultSet.getInt("account_id"));
                    account.setType(resultSet.getString("type"));
                    account.setBalance(resultSet.getBigDecimal("balance"));
                    account.setOwnerId(resultSet.getInt("owner_id"));
                    account.setPending(resultSet.getBoolean("pending"));
                    account.setCreatedAt(resultSet.getDate("created_at"));
                    accounts.add(account);
                }

                return accounts;
            }

        } catch (SQLException e) {
            throw new BusinessException("Internal error occurred. Please contact sysadmin");
        }
    }

    /**
     * Updates the account's balance in the database
     * @param accountId - The id of the account to be updated
     * @param newBalance - The new balance
     * @return - An account object with the updated balance
     * @throws BusinessException - If an SQLException happens or the account could not be updated for some reason
     */
    @Override
    public Account updateBalance(int accountId, BigDecimal newBalance) throws BusinessException {
        try(Connection connection = PostgresConnection.getConnection()) {
            String sql = "UPDATE bankapp_schema.accounts SET balance = ? WHERE account_id = ?";

            try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setBigDecimal(1, newBalance);
                ps.setInt(2, accountId);

                int c = ps.executeUpdate();
                if (c == 1) {
                    ResultSet resultSet = ps.getGeneratedKeys();
                    Account updatedAccount = new Account();
                    if (resultSet.next()) {
                        updatedAccount.setAccountId(resultSet.getInt("account_id"));
                        updatedAccount.setType(resultSet.getString("type"));
                        updatedAccount.setBalance(resultSet.getBigDecimal("balance"));
                        updatedAccount.setOwnerId(resultSet.getInt("owner_id"));
                        updatedAccount.setPending(resultSet.getBoolean("pending"));
                        updatedAccount.setCreatedAt(resultSet.getDate("created_at"));
                        return updatedAccount;
                    } else {
                        throw new BusinessException("Account could not be updated.");
                    }
                }
                return null;
            }
        } catch (SQLException e) {
            throw new BusinessException("Internal error occurred. Please contact sysadmin" + e);
        }
    }
}
