package org.bankapp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
    }

    @Test
    @DisplayName("Can create a new Customer and add it to the map of users")
    void canCreateAndAddUsers() {
        bank.addUser("Michael", "Keyes", "GuiltySpark87", "p@ssword123");
        bank.addUser("Sarah", "McLachlan", "Angel", "!nTh3@rmsOf@n@ng3l");
        Assertions.assertEquals(2, bank.getUsers().size());
    }

    @Test
    @DisplayName("a user can login")
    void canLoginUsers() {
        bank.loginUser("mrbubbles87", "p@ssword123");
    }
}