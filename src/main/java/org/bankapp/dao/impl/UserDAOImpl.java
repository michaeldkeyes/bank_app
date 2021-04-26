package org.bankapp.dao.impl;

import org.bankapp.dao.UserDAO;
import org.bankapp.dao.dbutil.PostgresConnection;
import org.bankapp.exception.BusinessException;
import org.bankapp.model.User;

import java.sql.*;

public class UserDAOImpl implements UserDAO {
    private final String sqlexceptionString = "SQLException occurred. Please contact sysadmin";

    /**
     * Adds a new user to the database
     * @param user - The user to add
     * @throws BusinessException - If an SQLException occurs or the result set does not return a value
     */
    @Override
    public void addUser(User user) throws BusinessException {
        try (Connection connection = PostgresConnection.getConnection()) {
            String sql = "INSERT INTO bankapp_schema.users (username, \"password\") VALUES(?, ?);\n";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPassword());

                int c = preparedStatement.executeUpdate();
                if (c != 1) throw new BusinessException("User could not be added.");

            }

        } catch (SQLException e) {
            throw new BusinessException(sqlexceptionString + e);
        }
    }

    /**
     * Gets a user from the database by the username
     * @param user - The user with the username to find
     * @return - The user found in the database. Can return null if a user is not found
     * @throws BusinessException - If an SQLException occurs
     */
    @Override
    public User getUserByUsername(User user) throws BusinessException {
        try (Connection connection = PostgresConnection.getConnection()) {
            User storedUser = new User();
            String sql = "SELECT id, username, \"password\" FROM bankapp_schema.users WHERE username = ?;\n";

            try(PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
            }

        } catch (SQLException e) {
            throw new BusinessException(sqlexceptionString + e);
        }
    }

    /**
     * Gets a user from the database by the id
     * @param user - The user who has the id to find
     * @return - The user found from the database. Can return null if no user is found.
     * @throws BusinessException
     */
    @Override
    public User getUserById(User user) throws BusinessException {
        try (Connection connection = PostgresConnection.getConnection()) {
            User storedUser = new User();
            String sql = "SELECT id, username, \"password\" FROM bankapp_schema.users WHERE id = ?;\n";

            try(PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, user.getId());

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    storedUser.setId(resultSet.getInt("id"));
                    storedUser.setUsername(resultSet.getString("username"));
                    storedUser.setPassword(resultSet.getString("password"));
                } else {
                    return null;
                }

                return storedUser;
            }

        } catch (SQLException e) {
            throw new BusinessException(sqlexceptionString + e);
        }
    }
}
