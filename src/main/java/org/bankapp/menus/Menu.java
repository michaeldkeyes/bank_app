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
    private final Logger logger = Logger.getLogger(Menu.class);
    private final Scanner input = new Scanner(System.in);

    private final String[] mainMenuOptions = {"Customer Login", "Employee Login", "Register", "Exit"};

    public int mainMenu () {
        int choice = 0;

        do {
            logger.info(MenuUtils.MENU_LINES);
            logger.info("Welcome to Bank App!");
            logger.info(MenuUtils.MENU_LINES);
            for (int i = 0; i < mainMenuOptions.length; i++) {
                logger.info((i+1) + ") " + mainMenuOptions[i]);
            }

            choice = MenuUtils.getChoice();

        } while (choice < 1 || choice > 4);

        return choice;
    }

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
            logger.info(MenuUtils.MENU_LINES);
            logger.info("Customer Login");
            logger.info(MenuUtils.MENU_LINES);
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

    public boolean employeeLoginMenu() {
        String userName;
        String password;
        int failedLoginAttempts = 0;

        while (failedLoginAttempts < 3) {
            logger.info(MenuUtils.MENU_LINES);
            logger.info("Employee Login");
            logger.info(MenuUtils.MENU_LINES);
            logger.info("Please enter your username: ");
            userName = input.nextLine();
            logger.info("\nPlease enter your password: ");
            password = input.nextLine();

            if (userName.equals("employee") && password.equals("employee")) {
                return true;
            } else {
                logger.info("\nUsername/password does not match.\n");
                failedLoginAttempts++;
                if (failedLoginAttempts == 3) {
                    logger.info("Too many login attempts. Please contact customer support");
                }
            }
        }
        return false;
    }
}
