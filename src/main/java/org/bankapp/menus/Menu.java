package org.bankapp.menus;

import org.apache.log4j.Logger;
import org.bankapp.exception.BusinessException;
import org.bankapp.model.Account;
import org.bankapp.model.User;
import org.bankapp.security.Security;
import org.bankapp.service.AccountService;
import org.bankapp.service.UserService;
import org.bankapp.service.impl.AccountServiceImpl;
import org.bankapp.service.impl.UserServiceImpl;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
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

            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                logger.info(e);
            }

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
            System.out.println(e);
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
     * Retrieves all the accounts the logged in user has then displays the menu for customers who are logged in.
     * @param authUser - The user who is currently logged in
     */
    public void customerMenu(User authUser) {
        int choice = 0;
        AccountService accountService = new AccountServiceImpl();

        try {
            List<Account> accounts = accountService.getAccountsByOwnerId(authUser.getId());
            authUser.setAccounts(accounts);
        } catch (BusinessException e) {
            logger.error(e);
        }

        while (choice != 8) {
            logger.info(menuLines);
            logger.info(String.format("Welcome, %s!", authUser.getUsername()));
            logger.info(menuLines);
            logger.info("How may we assist you today?\n");
            logger.info("1) View accounts");
            logger.info("2) Apply for a new account");
            logger.info("3) Withdraw");
            logger.info("4) Deposit");
            logger.info("5) Transfer to another account");
            logger.info("6) Transfer to another member");
            logger.info("7) Change password");
            logger.info("8) Logout");
            logger.info("\n Please make a selection: ");

            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                logger.info(e);
            }

            switch (choice) {
                case 1:
                    showUserAccounts(authUser);
                    break;
                case 2:
                    createAccount(authUser, accountService);
                    break;
                case 3:
                    System.out.println("Withdraw");
                    break;
                case 4:
                    System.out.println("Deposit");
                    break;
                case 5:
                    System.out.println("Transfer to another account");
                    break;
                case 6:
                    System.out.println("Transfer to another member");
                    break;
                case 7:
                    System.out.println("Change password");
                    break;
                case 8:
                    logger.info("Have a nice day!");
                default:
                    logger.info("Invalid choice. Please select an option from the menu above.");
            }
        }

    }

    /**
     * Displays the logged in user's accounts
     * @param authUser - The user who is currently logged in
     */
    public void showUserAccounts(User authUser) {
        int i = 1;
        for (Account account: authUser.getAccounts()) {
            logger.info(i + ") "
                    + account.getAccountId()
                    + " " + account.getType()
                    + " " + account.getBalance());
            i++;
        }
    }

    /**
     * Displays the menu to create an account and allows the user to select checking or savings
     * @param authUser - The user who is currently logged in
     */
    public void createAccount(User authUser, AccountService accountService) {
        int choice = 0;
        logger.info("What type of account are you applying for?");
        while (choice != 3) {
            logger.info("1) Checking");
            logger.info("2) Savings");
            logger.info("3) Go back");

            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                logger.info(e);
            }

            if (choice == 1 || choice == 2) {
                String accountType = choice == 1 ? "Checking" : "Savings";
                Account newUserAccount = new Account(accountType, authUser.getId());
                try {
                    newUserAccount = accountService.createAccount(newUserAccount);
                    if (newUserAccount != null) {
                        authUser.getAccounts().add(newUserAccount);
                        logger.info("Thank you for applying for an account with Bank App!");
                        logger.info("Your account will be approved in 3-5 business days");
                    }
                } catch (BusinessException e) {
                    logger.error(e);
                }
            }
            else if (choice == 3) logger.info("Going back");
            else logger.info("Invalid selection. Please select an option from the menu above");
        }
    }
}
