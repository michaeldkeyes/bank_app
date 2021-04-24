package org.bankapp.database;

import org.bankapp.users.Customer;
import org.bankapp.users.User;

import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "password";

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private Statement statement = null;

    public void insertCustomer(String firstName, String lastName, String username, String password) {
        String sql = "INSERT INTO \"BankApp\".users\n" +
                "(firstname, lastname, username, \"password\")\n" +
                "VALUES(?, ?, ?, ?);";

        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, password);

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }
    }

    public Customer findCustomer(String typedUsername) {
        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = connection.createStatement();

            String sql = "SELECT custid, firstname, lastname, username, \"password\"\n" +
                    "FROM \"BankApp\".users;\n";

            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                if (typedUsername.equals(rs.getString("username"))) {
                    int custId = rs.getInt("custid");
                    String firstName = rs.getString("firstname");
                    String lastName = rs.getString("lastname");
                    String username = rs.getString("username");
                    String password = rs.getString("password");

                    statement.close();
                    connection.close();
                    return new Customer(custId, firstName, lastName, username, password);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }

        return null;
    }
}
