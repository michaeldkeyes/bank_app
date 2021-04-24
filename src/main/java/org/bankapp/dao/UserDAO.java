package org.bankapp.dao;

import org.bankapp.exception.BusinessException;
import org.bankapp.model.User;

public interface UserDAO {
    public void addUser(User user) throws BusinessException;
    public User getUserByUsername(User user) throws BusinessException;
}
