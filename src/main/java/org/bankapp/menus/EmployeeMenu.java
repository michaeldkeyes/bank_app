package org.bankapp.menus;

import org.apache.log4j.Logger;

import java.util.Scanner;

public class EmployeeMenu {
    private final Logger logger = Logger.getLogger(EmployeeMenu.class);
    private static final String MENU_LINES = "============================================================";
    private final Scanner input = new Scanner(System.in);
    private final String[] employeeMainMenuChoices = {"View pending accounts", "Logout"};

    public void employeeMainMenu() {
        int choice = 0;
        while (choice != employeeMainMenuChoices.length) {
            logger.info(MENU_LINES);
            logger.info("Employee menu");
            logger.info(MENU_LINES);
            for (int i = 0; i < employeeMainMenuChoices.length; i++) {
                logger.info((i+1) + ") " + employeeMainMenuChoices[i]);
            }
            choice = MenuUtils.getChoice();
        }

    }


}
