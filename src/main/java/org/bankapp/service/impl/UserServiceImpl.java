package org.bankapp.service.impl;

import org.bankapp.dao.UserDAO;
import org.bankapp.dao.impl.UserDAOImpl;
import org.bankapp.exception.BusinessException;
import org.bankapp.model.User;
import org.bankapp.security.Security;
import org.bankapp.service.UserService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class UserServiceImpl implements UserService {
    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    public void addUser(User user) throws BusinessException {
        userDAO.addUser(user);
    }

    @Override
    public User getUserByUsername(User user) throws BusinessException {
        return userDAO.getUserByUsername(user);
    }

    @Override
    public boolean authorizeUser(User user, User storedUser) throws BusinessException {
        try {
            return Security.validatePassword(user.getPassword(), storedUser.getPassword());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println(e);
            throw new BusinessException("An internal error occurred. Please contact sysadmin");
        }
    }

    @Override
    public User getUserById(User user) throws BusinessException {
        return userDAO.getUserById(user);
    }
}
