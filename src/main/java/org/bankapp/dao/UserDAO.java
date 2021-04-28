package org.bankapp.dao;

import org.bankapp.exception.BusinessException;
import org.bankapp.model.User;

public interface UserDAO {
    void addUser(User user) throws BusinessException;
    User getUserByUsername(User user) throws BusinessException;
    User getUserById(User user) throws BusinessException;
}
