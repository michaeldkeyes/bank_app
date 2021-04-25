package org.bankapp.service;

import org.bankapp.exception.BusinessException;
import org.bankapp.model.User;

import java.sql.SQLException;

public interface UserService {
    public void addUser(User user) throws BusinessException;
    public User getUserByUsername(User user) throws BusinessException;
    public boolean authorizeUser(User user, User storedUser) throws BusinessException;
    public User getUserById(User user) throws BusinessException;
}
