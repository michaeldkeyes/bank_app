package org.bankapp.users;

import org.bankapp.Bank;

public class Customer extends User {
    private final String firstName;
    private final String lastName;
    private final int custId;

    public Customer(int custId, String firstName, String lastName, String username, String password) {
        super(username, password);
        this.custId = custId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
