package org.bankapp.menus;

import org.apache.log4j.Logger;
import org.bankapp.exception.BusinessException;
import org.bankapp.model.Account;
import org.bankapp.model.Transaction;
import org.bankapp.model.User;
import org.bankapp.service.AccountService;
import org.bankapp.service.TransactionService;
import org.bankapp.service.impl.AccountServiceImpl;
import org.bankapp.service.impl.TransactionServiceImpl;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.List;

public class CustomerMenu {

    private final Logger logger = Logger.getLogger(CustomerMenu.class);

    private final String[] customerMenuOptions = {"View Accounts", "Apply For A New Account", "Withdraw", "Deposit", "Transfer", "Logout"};
    private final String[] createAccountMenuOptions = {"Checking", "Savings", "Go Back"};

    private final AccountService accountService = new AccountServiceImpl();

    public CustomerMenu(User authUser) {
        try {
            List<Account> accounts = accountService.getAccountsByOwnerId(authUser.getId());
            authUser.setAccounts(accounts);
        } catch (BusinessException e) {
            logger.error(e);
        }
    }

    /**
     * Retrieves all the accounts the logged in user has then displays the menu for customers who are logged in.
     *
     * @param authUser - The user who is currently logged in
     */
    public void customerMenu(User authUser) {
        int choice = 0;
        final int maxNumAccounts = 2;
        final String noAccounts = "You currently do not have any accounts available. "
                + "Either apply for a new account, or wait for your account to be approved";

        while (choice != customerMenuOptions.length) {
            logger.info(MenuUtils.MENU_LINES);
            logger.info(String.format("Welcome, %s!", authUser.getUsername()));
            logger.info(MenuUtils.MENU_LINES);
            logger.info("How may we assist you today?\n");
            for (int i = 0; i < customerMenuOptions.length; i++) {
                logger.info((i + 1) + ") " + customerMenuOptions[i]);
            }
            logger.info("\n Please make a selection: ");

            choice = MenuUtils.getChoice();

            switch (choice) {
                case 1:
                    showUserAccountsMenu(authUser);
                    break;
                case 2:
                    if (authUser.getAccounts().size() < maxNumAccounts) createAccountMenu(authUser);
                    else logger.info("You already have the maximum number of accounts allowed\n");
                    break;
                case 3:
                    if (authUser.getAccounts().isEmpty()) logger.info(noAccounts);
                    withdrawMenu(authUser);
                    break;
                case 4:
                    if (authUser.getAccounts().isEmpty()) logger.info(noAccounts);
                    depositMenu(authUser);
                    break;
                case 5:
                    transferMenu(authUser);
                    break;
                case 6:
                    logger.info("Have a nice day!");
                    break;
                default:
                    logger.info("Invalid choice. Please select an option from the menu above.");
            }
        }
    }

    /**
     * Displays all of the logged in user's accounts
     *
     * @param authUser - The user who is currently logged in
     */
    private void showUserAccountsMenu(User authUser) {
        int choice = 0;
        List<Account> accounts = authUser.getAccounts();

        while (choice != accounts.size() + 1) {
            showNonPendingAccounts(authUser);
            logger.info((accounts.size() + 1) + ") Go back");
            logger.info("Select an account to view transactions");
            choice = MenuUtils.getChoice();
            if (choice > 0 && choice <= accounts.size()) {
                TransactionService ts = new TransactionServiceImpl();
                try {
                    List<Transaction> transactions = ts.getAllTransactionsByAccount(accounts.get(choice - 1).getAccountId());
                    if (!transactions.isEmpty()) {
                        for (Transaction transaction : transactions) {
                            logger.info(transaction.getMemo() + " " + transaction.getTimestamp());
                        }
                    } else logger.info("You have no transactions on this account");
                } catch (BusinessException e) {
                    logger.fatal("e");
                }
            }
        }

    }

    /**
     * Displays the logged in user's approved accounts
     *
     * @param authUser - The user who is currently logged in
     */
    private void showNonPendingAccounts(User authUser) {
        int i = 1;
        for (Account account : authUser.getAccounts()) {
            if (!account.isPending()) {
                logger.info(i + ") "
                        + account.getAccountId()
                        + " " + account.getType()
                        + " " + account.getBalance());
            } else {
                logger.info("This account is pending " + account.getAccountId() + " " + account.getType());
            }
            i++;
        }
    }

    /**
     * Displays the menu to create an account and allows the user to select checking or savings
     *
     * @param authUser - The user who is currently logged in
     */
    private void createAccountMenu(User authUser) {
        int choice = 0;
        logger.info("What type of account are you applying for?");
        while (choice != createAccountMenuOptions.length) {
            for (int i = 0; i < createAccountMenuOptions.length; i++) {
                logger.info((i + 1) + ") " + createAccountMenuOptions[i]);
            }
            try {
                choice = MenuUtils.getChoice();

                if (choice == 1 || choice == 2) {
                    String accountType = choice == 1 ? "Checking" : "Savings";
                    Account newUserAccount = new Account(accountType, authUser.getId());
                    newUserAccount = accountService.createAccount(newUserAccount);
                    authUser.getAccounts().add(newUserAccount);
                    logger.info("Thank you for applying for an account with Bank App!");
                    logger.info("Your account will be approved in 3-5 business days");
                    break;
                }
                logger.info(MenuUtils.BAD_INPUT_MESSAGE);
            } catch (NumberFormatException e) {
                logger.info(MenuUtils.BAD_INPUT_MESSAGE);
            } catch (BusinessException e) {
                logger.error(e);
            }
        }
    }

    /**
     * Displays the menu that withdraws or deposits an amount that the user inputs
     *
     * @param authUser     - The user who is currently logged in
     */
    private void depositMenu(User authUser) {
        try {
            int choice = 0;
            List<Account> accounts = authUser.getAccounts();
            while (choice != accounts.size() + 1) {
                showNonPendingAccounts(authUser);
                logger.info(authUser.getAccounts().size() + 1 + MenuUtils.GO_BACK);
                logger.info("Select one of your accounts: ");
                choice = MenuUtils.getChoice();     // NumberFormatException thrown here

                if (choice > 0 && choice <= accounts.size() && !accounts.get(choice - 1).isPending()) {
                    BigDecimal amount;
                    logger.info("\nEnter the amount you wish to deposit:");

                    amount = MenuUtils.getBigDecimal();     // InputMismatchException thrown here
                    if (amount.compareTo(BigDecimal.ZERO) > 0) {
                        StringBuilder memo = new StringBuilder("Deposit ");
                        Account accountToUpdate = accounts.get(choice - 1);
                        TransactionService ts = new TransactionServiceImpl();
                        accountService.updateBalance(accountToUpdate, amount);      // BusinessException thrown here
                        memo.append(amount.toString());
                        int accountId = accountToUpdate.getAccountId();
                        ts.createTransaction(accountId, accountId, memo.toString(), amount);    // Or here
                    }
                }
            }
        } catch (NumberFormatException | InputMismatchException e) {
            logger.error(MenuUtils.BAD_INPUT_MESSAGE);
        } catch (BusinessException e) {
            logger.fatal(e);
        }
    }

    private void withdrawMenu(User authUser) {
        int choice = 0;
        List<Account> accounts = authUser.getAccounts();
        while (choice != accounts.size() + 1) {
            try {
                showNonPendingAccounts(authUser);
                logger.info(authUser.getAccounts().size() + 1 + MenuUtils.GO_BACK);
                logger.info("Select one of your accounts: ");
                choice = MenuUtils.getChoice();     // NumberFormatException thrown here

                if (choice > 0 && choice <= accounts.size() && !accounts.get(choice - 1).isPending()) {
                    BigDecimal amount;
                    logger.info("\nEnter the amount you wish to withdraw:");

                    amount = MenuUtils.getBigDecimal();     // InputMismatchException thrown here
                    if (amount.compareTo(BigDecimal.ZERO) > 0) {
                        StringBuilder memo = new StringBuilder("Withdraw ");
                        amount = amount.negate();
                        if (accounts.get(choice - 1).getBalance().compareTo(amount) < 0) {
                            logger.info("\nYou cannot withdraw more than your account balance.");
                            choice = 0;
                            continue;
                        }

                        Account accountToUpdate = accounts.get(choice - 1);
                        TransactionService ts = new TransactionServiceImpl();
                        accountService.updateBalance(accountToUpdate, amount);      // BusinessException thrown here
                        memo.append(amount.toString());
                        int accountId = accountToUpdate.getAccountId();
                        ts.createTransaction(accountId, accountId, memo.toString(), amount);    // Or here
                    }
                }
            } catch (NumberFormatException | InputMismatchException e) {
                logger.error(MenuUtils.BAD_INPUT_MESSAGE);
            } catch (BusinessException e) {
                logger.fatal(e);
            }

        }
    }

    /**
     * Displays the menu and receives input to transfer money between accounts
     *
     * @param authUser - The user that is logged in
     */
    private void transferMenu(User authUser) {
        int choice = 0;

        while (choice != authUser.getAccounts().size() + 1) {
            showNonPendingAccounts(authUser);
            logger.info(authUser.getAccounts().size() + 1 + MenuUtils.GO_BACK);
            logger.info("Select one of your accounts that you wish to transfer from: ");
            choice = MenuUtils.getChoice();

            if (choice > 0 && choice <= authUser.getAccounts().size()) {
                int transferTo;
                logger.info("Please enter the account number you wish to transfer to");
                transferTo = MenuUtils.getChoice();
                try {
                    Account toAccount = accountService.getAccountById(transferTo);
                    Account fromAccount = authUser.getAccounts().get(choice - 1);
                    logger.info("Enter the amount you wish to transfer:");
                    BigDecimal amount = MenuUtils.getBigDecimal();
                    if (amount.compareTo(BigDecimal.ZERO) > 0) {
                        accountService.transfer(fromAccount, toAccount, amount);
                        String memo = String.format("Transfer of %s from account %d to account %d ", amount, fromAccount.getAccountId(), toAccount.getAccountId());
                        TransactionService ts = new TransactionServiceImpl();
                        ts.createTransaction(fromAccount.getAccountId(), toAccount.getAccountId(), memo, amount);
                        logger.info(memo);
                        authUser.setAccounts(accountService.getAccountsByOwnerId(authUser.getId()));
                    }
                } catch (NumberFormatException | InputMismatchException e) {
                    logger.error(MenuUtils.BAD_INPUT_MESSAGE);
                } catch (BusinessException e) {
                    logger.fatal(e);
                }
            }
        }
    }
}
