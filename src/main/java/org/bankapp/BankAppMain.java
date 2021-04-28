package org.bankapp;

import org.apache.log4j.Logger;
import org.bankapp.menus.CustomerMenu;
import org.bankapp.menus.EmployeeMenu;
import org.bankapp.menus.MainMenu;
import org.bankapp.model.User;

public class BankAppMain {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(BankAppMain.class);

        MainMenu mainMenu = new MainMenu();
        int selection = 0;
        while (selection != 4) {
            selection = mainMenu.mainMenu();
            switch (selection) {
                case 1:
                    User authUser = mainMenu.customerLoginMenu();
                    if (authUser != null) {
                        CustomerMenu cm = new CustomerMenu(authUser);
                        cm.customerMenu(authUser);
                    }
                    break;
                case 2:
                    if (mainMenu.employeeLoginMenu()) {
                        EmployeeMenu employeeMenu = new EmployeeMenu();
                        employeeMenu.employeeMainMenu();
                    }
                    break;
                case 3:
                    mainMenu.registerMenu();
                    break;
                default:
                    logger.info("Thank you for using Bank App!");
            }
        }

    }

}
