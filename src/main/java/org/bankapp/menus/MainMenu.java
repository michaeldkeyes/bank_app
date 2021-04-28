package org.bankapp.menus;

import org.apache.log4j.Logger;
import org.bankapp.exception.BusinessException;
import org.bankapp.model.User;
import org.bankapp.security.Security;
import org.bankapp.service.UserService;
import org.bankapp.service.impl.UserServiceImpl;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class MainMenu {
    private final Logger logger = Logger.getLogger(MainMenu.class);

    private final String[] mainMenuOptions = {"Customer Login", "Employee Login", "Register", "Exit"};

    /**
     * Displays the main menu
     *
     * @return - The choice the user made
     */
    public int mainMenu() {
        int choice = 0;

        while (choice < 1 || choice > mainMenuOptions.length) {
            try {
                logger.info(MenuUtils.MENU_LINES);
                logger.info("Welcome to Bank App!");
                logger.info(MenuUtils.MENU_LINES);
                for (int i = 0; i < mainMenuOptions.length; i++) {
                    logger.info((i + 1) + ") " + mainMenuOptions[i]);
                }

                choice = MenuUtils.getChoice();
            } catch (NumberFormatException e) {
                logger.error(MenuUtils.BAD_INPUT_MESSAGE);
            }
        }

        return choice;
    }

    /**
     * Displays and receives the input required to make a new user account. Checks username against a basic regex expression.
     * The user will enter their desired password twice to match. The password is then hashed and the new user is added to the database.
     */
    public void registerMenu() {
        String userName;
        String password;
        String confirmPassword;
        UserService userService = new UserServiceImpl();

        User user = new User();

        logger.info(MenuUtils.MENU_LINES);
        logger.info("Register");
        logger.info(MenuUtils.MENU_LINES);

        do {
            logger.info("Enter your username: ");
            userName = MenuUtils.getString();
            if (!userName.matches("[a-zA-Z0-9]{5,30}")) {
                logger.info("\nInvalid username. Please enter your username: ");
            }
        } while (!userName.matches("[a-zA-Z0-9]{5,30}"));
        user.setUsername(userName);

        do {
            logger.info("\nEnter your password: ");
            password = MenuUtils.getString();
            logger.info("\nConfirm your password");
            confirmPassword = MenuUtils.getString();
            if (!password.equals(confirmPassword)) {
                logger.info("\nPasswords do not match!");
            }
        } while (!password.equals(confirmPassword));

        try {
            password = Security.generateStrongPasswordHash(password);
            user.setPassword(password);
            userService.addUser(user);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.fatal("A fatal error occurred. Please contact sysadmin. " + e);
        } catch (BusinessException e) {
            logger.error(e);
        }
        logger.info("\nThank you for signing up with BankApp!");
    }

    /**
     * Displays the menu for the user to login.
     *
     * @return - The authorized user if credentials are correct. Returns null if three unsuccessful login attempts are made.
     */
    public User customerLoginMenu() {
        User authUser = new User();
        User storedUser;
        String userName;
        String password;
        int failedLoginAttempts = 0;
        final int maxLoginAttempts = 3;
        UserService userService = new UserServiceImpl();

        while (failedLoginAttempts < maxLoginAttempts) {
            logger.info(MenuUtils.MENU_LINES);
            logger.info("Customer Login");
            logger.info(MenuUtils.MENU_LINES);
            logger.info("Please enter your username: ");
            userName = MenuUtils.getString();
            logger.info("\nPlease enter your password: ");
            password = MenuUtils.getString();

            authUser.setUsername(userName);
            authUser.setPassword(password);

            try {
                storedUser = userService.getUserByUsername(authUser);
                if (storedUser != null && userService.authorizeUser(authUser, storedUser)) {
                    return storedUser;
                } else {
                    logger.info("\nUsername/password does not match.\n");
                    failedLoginAttempts++;
                    if (failedLoginAttempts == maxLoginAttempts)
                        logger.info("Too many login attempts. Please contact customer support");
                }
            } catch (BusinessException e) {
                logger.error(e);
            }
        }

        return null;
    }

    /**
     * Displays the menu for an employee to login.
     *
     * @return - True if the employee logged in successfully. False if there are three unsuccessful login attempts.
     */
    public boolean employeeLoginMenu() {
        String userName;
        String password;
        int failedLoginAttempts = 0;
        final int maxAttempts = 3;

        while (failedLoginAttempts < maxAttempts) {
            logger.info(MenuUtils.MENU_LINES);
            logger.info("Employee Login");
            logger.info(MenuUtils.MENU_LINES);
            logger.info("Please enter your username: ");
            userName = MenuUtils.getString();
            logger.info("\nPlease enter your password: ");
            password = MenuUtils.getString();

            if (userName.equals("employee") && password.equals("employee")) {
                return true;
            } else {
                logger.info("\nUsername/password does not match.\n");
                failedLoginAttempts++;
                if (failedLoginAttempts == maxAttempts) {
                    logger.info("Too many login attempts. Please contact customer support");
                }
            }
        }
        return false;
    }
}
