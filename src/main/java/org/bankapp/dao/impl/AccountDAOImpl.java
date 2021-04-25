package org.bankapp.dao.impl;

import org.bankapp.dao.AccountDAO;
import org.bankapp.dao.dbutil.PostgresConnection;
import org.bankapp.exception.BusinessException;
import org.bankapp.model.Account;

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
            System.out.println(e);
            throw new BusinessException("Internal error occured. Please contact sysadmin");
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
                    account.setBalance(resultSet.getFloat("balance"));
                    account.setOwnerId(resultSet.getInt("owner_id"));
                    account.setPending(resultSet.getBoolean("pending"));
                    account.setCreatedAt(resultSet.getDate("created_at"));
                    accounts.add(account);
                }

                return accounts;
            }

        } catch (SQLException e) {
            System.out.println(e);
            throw new BusinessException("Internal error occurred. Please contact sysadmin");
        }
    }
}
