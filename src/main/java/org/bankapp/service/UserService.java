package org.bankapp.service;

import org.bankapp.exception.BusinessException;
import org.bankapp.model.User;

public interface UserService {
    void addUser(User user) throws BusinessException;
    User getUserByUsername(User user) throws BusinessException;
    boolean authorizeUser(User user, User storedUser) throws BusinessException;
    User getUserById(User user) throws BusinessException;
}
