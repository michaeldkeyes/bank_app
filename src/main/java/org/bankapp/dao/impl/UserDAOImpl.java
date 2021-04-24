package org.bankapp.dao.impl;

import org.bankapp.dao.UserDAO;
import org.bankapp.dao.dbutil.PostgresConnection;
import org.bankapp.exception.BusinessException;
import org.bankapp.model.User;

import java.sql.*;

public class UserDAOImpl implements UserDAO {
    @Override
    public void addUser(User user) throws BusinessException {
        try (Connection connection = PostgresConnection.getConnection()) {
            String sql = "INSERT INTO bankapp_schema.users (username, \"password\") VALUES(?, ?);\n";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());

            int c = preparedStatement.executeUpdate();
            if (c == 1) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    System.out.println("Successfully added user: " + user);
                } else {
                    throw new BusinessException("User could not be added.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new BusinessException("Internal error occured. Please contact sysadmin");
        }
    }

    @Override
    public User getUserByUsername(User user) throws BusinessException {
        try (Connection connection = PostgresConnection.getConnection()) {
            User storedUser = new User();
            String sql = "SELECT id, username, \"password\" FROM bankapp_schema.users WHERE username = ?;\n";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getUsername());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                storedUser.setId(resultSet.getInt("id"));
                storedUser.setUsername(resultSet.getString("username"));
                storedUser.setPassword(resultSet.getString("password"));
            } else {
                return null;
            }

            return storedUser;
        } catch (SQLException e) {
            System.out.println(e);
            throw new BusinessException("Internal error occurred. Please contact sysadmin");
        }
    }
}
