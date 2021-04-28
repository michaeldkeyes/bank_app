package org.bankapp.menus;

import org.apache.log4j.Logger;
import org.bankapp.exception.BusinessException;
import org.bankapp.model.Account;
import org.bankapp.service.AccountService;
import org.bankapp.service.impl.AccountServiceImpl;

import java.util.List;

public class EmployeeMenu {
    private final Logger logger = Logger.getLogger(EmployeeMenu.class);
    private final String[] employeeMainMenuChoices = {"View pending accounts", "Logout"};
    private final AccountService accountService;

    public EmployeeMenu() {
        this.accountService = new AccountServiceImpl();
    }

    /**
     * Displays the main employee menu. Contains all the options available to the employee
     */
    public void employeeMainMenu() {
        int choice = 0;
        while (choice != employeeMainMenuChoices.length) {
            logger.info(MenuUtils.MENU_LINES);
            logger.info("Employee menu");
            logger.info(MenuUtils.MENU_LINES);
            for (int i = 0; i < employeeMainMenuChoices.length; i++) {
                logger.info((i + 1) + ") " + employeeMainMenuChoices[i]);
            }
            choice = MenuUtils.getChoice();

            switch (choice) {
                case 1:
                    pendingAccountsMenu();
                    break;
                case 2:
                    logger.info("Logging off\n");
                    break;
                default:
                    logger.info("Invalid input. You're fired.");
            }
        }

    }

    /**
     * Displays all the pending accounts from the database and allows the employee to approve a pending account
     */
    public void pendingAccountsMenu() {
        try {
            List<Account> pendingAccounts = accountService.getAccountsByPending(true);
            int choice = 0;
            while (choice != pendingAccounts.size() + 1) {
                int i = 0;
                for (Account account : pendingAccounts) {
                    logger.info((i + 1) + ") " + account.toString());
                    i++;
                }
                logger.info((i + 1) + ")Go Back");
                logger.info("Select an account to approve the application");
                choice = MenuUtils.getChoice();
                if (choice > 0 && choice <= pendingAccounts.size()) {
                    accountService.updatePending(pendingAccounts.get(choice - 1), false);
                    logger.info("Account has been approved");
                    pendingAccounts = accountService.getAccountsByPending(true);
                } else logger.info(MenuUtils.BAD_INPUT_MESSAGE);
            }
        } catch (BusinessException e) {
            logger.error(e);
        }
    }

}
