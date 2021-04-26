package org.bankapp.menus;

import org.apache.log4j.Logger;
import org.bankapp.exception.BusinessException;
import org.bankapp.model.User;
import org.bankapp.security.Security;
import org.bankapp.service.UserService;
import org.bankapp.service.impl.UserServiceImpl;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

public class Menu {
    private final Logger logger;
    private final String menuLines;
    private final Scanner input = new Scanner(System.in);

    public Menu() {
        this.logger = Logger.getLogger(Menu.class);
        this.menuLines = "============================================================";
    }

    public int mainMenu () {
        int choice = 0;

        do {
            logger.info(menuLines);
            logger.info("Welcome to Bank App!");
            logger.info(menuLines);
            logger.info("1) Customer Login");
            logger.info("2) Employee Login");
            logger.info("3) Register");
            logger.info("4) Exit");

            choice = getChoice();

        } while (choice < 1 || choice > 4);

        return choice;
    }

    public void registerMenu() {
        String userName;
        String password;
        String confirmPassword;
        UserService userService = new UserServiceImpl();

        User user = new User();

        logger.info(menuLines);
        logger.info("Register");
        logger.info(menuLines);

        do {
            logger.info("Enter your username: ");
            userName = input.nextLine();
            if (!userName.matches("[a-zA-Z0-9]{5,30}")) {
                logger.info("\nInvalid username. Please enter your username: ");
                input.nextLine();
            }
        } while (!userName.matches("[a-zA-Z0-9]{5,30}"));
        user.setUsername(userName);

        do {
            logger.info("\nEnter your password: ");
            password = input.nextLine();
            logger.info("\nConfirm your password");
            confirmPassword = input.nextLine();
            if (!password.equals(confirmPassword)) {
                logger.info("\nPasswords do not match!");
            }
        } while (!password.equals(confirmPassword));

        try {
            password = Security.generateStrongPasswordHash(password);
            user.setPassword(password);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.fatal("A fatal error occurred. Please contact sysadmin.");
        }

        try {
            userService.addUser(user);
        } catch (BusinessException e) {
            logger.error(e);
        }
        logger.info("\nThank you for signing up with BankApp!");
    }

    public User customerLoginMenu() {
        User authUser = new User();
        User storedUser;
        String userName;
        String password;
        int failedLoginAttempts = 0;
        UserService userService = new UserServiceImpl();

        while (failedLoginAttempts < 3) {
            logger.info(menuLines);
            logger.info("Customer Login");
            logger.info(menuLines);
            logger.info("Please enter your username: ");
            userName = input.nextLine();
            logger.info("\nPlease enter your password: ");
            password = input.nextLine();

            authUser.setUsername(userName);
            authUser.setPassword(password);

            try {
                storedUser = userService.getUserByUsername(authUser);
                if (storedUser != null && userService.authorizeUser(authUser, storedUser)) {
                    return storedUser;
                } else {
                    logger.info("\nUsername/password does not match.\n");
                    failedLoginAttempts++;
                    if (failedLoginAttempts == 3) logger.info("Too many login attempts. Please contact customer support");
                }
            } catch (BusinessException e) {
                logger.error(e);
            }
        }

        return null;
    }

    public void employeeLoginMenu() {
        System.out.println("Employee login menu");
    }

    /**
     * Gets input from a user and then tries to parse that input to an int
     * @return the integer the user input
     */
    private int getChoice() {
        int choice = 0;
        try {
            choice = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            logger.info(e);
        }
        return choice;
    }

}
