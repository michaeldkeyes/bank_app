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

    /**
     * Gets an account from the database by its id
     * @param accountId - The accountId to search
     * @return - An account if one is found
     * @throws BusinessException - If an account is not found or an SQLException occurs
     */
    @Override
    public Account getAccountById(int accountId) throws BusinessException {
        try(Connection connection = PostgresConnection.getConnection()) {
            String sql = "SELECT account_id, \"type\", balance, owner_id, pending, created_at FROM bankapp_schema.accounts where account_id = ?;\n";
            try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, accountId);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Account account = new Account();
                    account.setAccountId(rs.getInt("account_id"));
                    account.setType(rs.getString("type"));
                    account.setBalance(rs.getBigDecimal("balance"));
                    account.setOwnerId(rs.getInt("owner_id"));
                    account.setPending(rs.getBoolean("pending"));
                    account.setCreatedAt(rs.getDate("created_at"));
                    return account;
                } else throw new BusinessException(("Account could not be found"));
            }
        } catch (SQLException e) {
            throw new BusinessException("Internal error occured. Please contact sysadmin " + e);
        }
    }

    /**
     * Gets all the accounts owned by a single user
     * @param ownerId - The owner of the accounts you want to fetch
     * @return - A list of accounts owned by the user
     * @throws BusinessException - If an SQLException occurs
     */
    @Override
    public List<Account> getAccountsByOwnerId(int ownerId) throws BusinessException {
        try(Connection connection = PostgresConnection.getConnection()) {
            String sql = "SELECT account_id, \"type\", balance, owner_id, pending, created_at FROM bankapp_schema.accounts where owner_id = ?;\n";

            try(PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, ownerId);

                ResultSet rs = preparedStatement.executeQuery();
                List<Account> accounts = new ArrayList<>();
                while (rs.next()) {
                    Account account = new Account();
                    account.setAccountId(rs.getInt("account_id"));
                    account.setType(rs.getString("type"));
                    account.setBalance(rs.getBigDecimal("balance"));
                    account.setOwnerId(rs.getInt("owner_id"));
                    account.setPending(rs.getBoolean("pending"));
                    account.setCreatedAt(rs.getDate("created_at"));
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
     * @param account - The account to be updated
     * @param newBalance - The new balance
     * @return - An account object with the updated balance
     * @throws BusinessException - If an SQLException happens or the account could not be updated for some reason
     */
    @Override
    public void updateBalance(Account account, BigDecimal newBalance) throws BusinessException {
        try(Connection connection = PostgresConnection.getConnection()) {
            String sql = "UPDATE bankapp_schema.accounts SET balance = ? WHERE account_id = ?";

            try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setBigDecimal(1, newBalance);
                ps.setInt(2, account.getAccountId());

                int c = ps.executeUpdate();
                if (c == 1) {
                    ResultSet resultSet = ps.getGeneratedKeys();
                    if (resultSet.next()) {
                        account.setBalance(resultSet.getBigDecimal("balance"));
                    } else {
                        throw new BusinessException("Account could not be updated.");
                    }
                }
            }
        } catch (SQLException e) {
            throw new BusinessException("Internal error occurred. Please contact sysadmin" + e);
        }
    }
}
