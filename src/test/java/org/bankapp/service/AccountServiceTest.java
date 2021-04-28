package org.bankapp.service;

import org.bankapp.dao.AccountDAO;
import org.bankapp.dao.dbutil.PostgresConnection;
import org.bankapp.dao.impl.AccountDAOImpl;
import org.bankapp.model.Account;
import org.bankapp.service.impl.AccountServiceImpl;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class AccountServiceTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement ps;

    @Mock
    private ResultSet rs;

    Account account;

    @InjectMocks
    private AccountDAO dao;

    @BeforeEach
    public void setUp() throws Exception {
        try (MockedStatic<PostgresConnection> mockPost = mockStatic(PostgresConnection.class)){
            mockPost.when(PostgresConnection::getConnection).thenReturn(mockConnection);
            //PostgresConnection.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString(), (int[]) any())).thenReturn(ps);
            doNothing().when(ps).setString(anyInt(), anyString());
            doNothing().when(ps).setInt(anyInt(), anyInt());
            doNothing().when(ps).setDate(anyInt(), any());

            account = new Account();
            account.setAccountId(1);
            account.setType("Checking");
            account.setOwnerId(1);
            account.setCreatedAt(new Date());
            System.out.println(account);

            when(ps.executeUpdate()).thenReturn(1);
            when(ps.getGeneratedKeys()).thenReturn(rs);
            when(rs.next()).thenReturn(true);
            when(rs.getInt(1)).thenReturn(account.getAccountId());
            when(rs.getDate(2)).thenReturn(java.sql.Date.valueOf(java.time.LocalDate.now()));
        }
    }

    @Test
    void createAccountTest() throws Exception{
        System.out.println(account);
        dao = new AccountDAOImpl();
        dao.createAccount(account);
        verify(mockConnection, times(1)).prepareStatement(anyString(), (int[]) any());
    }
}