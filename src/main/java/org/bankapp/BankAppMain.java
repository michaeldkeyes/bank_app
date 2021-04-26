package org.bankapp;

import org.apache.log4j.Logger;
import org.bankapp.menus.CustomerMenu;
import org.bankapp.menus.EmployeeMenu;
import org.bankapp.menus.Menu;
import org.bankapp.model.User;

public class BankAppMain {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(BankAppMain.class);
        User employee = new User("employee", "employee");

        Menu menu = new Menu();
        int selection = 0;
        while (selection != 4) {
            selection = menu.mainMenu();
            switch (selection) {
                case 1:
                    User authUser = menu.customerLoginMenu();
                    if (authUser != null) {
                        CustomerMenu cm = new CustomerMenu(authUser);
                        cm.customerMenu(authUser);
                    }
                    break;
                case 2:
                    if (menu.employeeLoginMenu()) {
                        EmployeeMenu employeeMenu = new EmployeeMenu();
                        employeeMenu.employeeMainMenu();
                    }
                    break;
                case 3:
                    menu.registerMenu();
                    break;
                case 4:
                    logger.info("Thank you for using Bank App!");
                    break;
                default:
                    logger.info("How did you get here? Bye I guess.");
            }
        }

    }

}
