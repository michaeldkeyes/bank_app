package org.bankapp.menus;

import org.apache.log4j.Logger;

import java.util.Scanner;

public class MenuUtils {
    private static final Logger logger = Logger.getLogger(MenuUtils.class);
    public static final String MENU_LINES = "============================================================";
    private static final Scanner input = new Scanner(System.in);

    /**
     * Gets input from a user and then tries to parse that input to an int
     * @return the integer the user input
     */
    public static int getChoice() {
        int choice = 0;
        try {
            choice = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            logger.info(e);
        }
        return choice;
    }
}
