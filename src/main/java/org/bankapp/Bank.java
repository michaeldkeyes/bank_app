package org.bankapp;

import org.apache.log4j.Logger;
import org.bankapp.database.DatabaseManager;
import org.bankapp.security.Security;
import org.bankapp.users.Customer;
import org.bankapp.users.User;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bank {

    private static final Logger logger = Logger.getLogger(Bank.class);

    private Random random;
    private List<Customer> users;
    Security security;
    DatabaseManager databaseManager;

    public Bank() {
        users = new ArrayList<>();
        security = new Security();
        databaseManager = new DatabaseManager();

        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
            System.exit(1);
        }
    }

    public void addUser(String firstName, String lastName, String userName, String password) {
        String securePassword = "";
        try {
            securePassword = security.generateStrongPasswordHash(password);
            logger.info(securePassword);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error(e);
        }
        if (securePassword != "") {
            databaseManager.insertCustomer(firstName, lastName, userName, securePassword);
        } else logger.error("Something went wrong hashing the password :(");

    }

    public List<Customer> getUsers() {
        return users;
    }

    public User loginUser(String username, String password) {
        // Look up User by username in the database
        Customer customer = databaseManager.findCustomer(username);
        // If the User was found, compare the typed password to the saved password
        if (customer != null) {
            System.out.println("Hooray!");
        } else System.out.println("What the fuck");

        return customer;
    }
}
