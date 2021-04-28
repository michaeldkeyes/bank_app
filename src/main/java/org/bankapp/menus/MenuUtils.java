package org.bankapp.menus;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuUtils {
    public static final String MENU_LINES = "============================================================";
    public static final String GO_BACK = ") Go back\n";
    public static final String BAD_INPUT_MESSAGE = "\nInvalid input/selection. Please select an option from the menu above";
    private static final Scanner input = new Scanner(System.in);

    private MenuUtils() {
    }

    /**
     * Gets input from a user and then tries to parse that input to an int
     * @return - the integer the user inputs
     */
    public static int getChoice() throws NumberFormatException{
        int choice;

        choice = Integer.parseInt(input.nextLine());

        return choice;
    }

    /**
     * Gets BigDecimal input from the user and sets the scale to two. This is for receiving monetary input
     * @return - The BigDecimal the user input
     */
    public static BigDecimal getBigDecimal() throws InputMismatchException {
        BigDecimal amount;

        amount = input.nextBigDecimal().setScale(2, BigDecimal.ROUND_DOWN);
        input.nextLine();

        return amount;
    }

    /**
     * Gets string input from the user
     * @return - The string the user input
     */
    public static String getString() {
        String str;
        str = input.nextLine();
        return str;
    }
}
