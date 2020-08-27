package org.academiadecodigo.warpers.services;

import org.academiadecodigo.warpers.exceptions.*;
import org.academiadecodigo.warpers.persistence.dao.SubscriptionDao;
import org.academiadecodigo.warpers.persistence.dao.UserDao;
import org.academiadecodigo.warpers.persistence.model.AbstractModel;
import org.academiadecodigo.warpers.persistence.model.User;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private SubscriptionDao subscriptionDao;

    /**
     * Sets the user data access object
     *
     * @param userDao the account DAO to set
     */
    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }


    /**
     * Sets the account data access object
     *
     * @param subscriptionDao the account DAO to set
     */
    @Autowired
    public void setSubscriptionDao(SubscriptionDao subscriptionDao) {
        this.subscriptionDao = subscriptionDao;
    }

    /**
     * @see UserService#get(Integer)
     */
    @Override
    public User get(Integer id) {
        return userDao.findById(id);
    }

    @Transactional
    @Override
    public User save(User user) {
        return userDao.saveOrUpdate(user);
    }

    /**
     * @see UserService#delete(Integer)
     */
    @Transactional
    @Override
    public void delete(Integer id) throws UserNotFoundException, AssociationExistsException {

        User user = Optional.ofNullable(userDao.findById(id))
                .orElseThrow(UserNotFoundException::new);

        if (!user.getSubscriptions().isEmpty()) {
            throw new AssociationExistsException();
        }

        userDao.delete(id);
    }

    /**
     * @see UserService#list()
     */
    @Override
    public List<User> list() {
        return userDao.findAll();
    }

    @Transactional
    @Override
    public Subscription addAccount(Integer id, Subscription subscription) throws UserNotFoundException, TransactionInvalidException {

        User user = Optional.ofNullable(userDao.findById(id))
                .orElseThrow(UserNotFoundException::new);

        user.addAccount(subscription);
        userDao.saveOrUpdate(user);

        return user.getSubscriptions().get(user.getSubscriptions().size() - 1);
    }

    /**
     * @see UserService#closeAccount(Integer, Integer)
     */
    @Transactional
    @Override
    public void closeAccount(Integer id, Integer accountId)
            throws UserNotFoundException, AccountNotFoundException, TransactionInvalidException {

        User user = Optional.ofNullable(userDao.findById(id))
                .orElseThrow(UserNotFoundException::new);

        Subscription subscription = Optional.ofNullable(subscriptionDao.findById(accountId))
                .orElseThrow(AccountNotFoundException::new);

        if (!subscription.getUser().getId().equals(id)) {
            throw new AccountNotFoundException();
        }

        user.removeAccount(subscription);
        userDao.saveOrUpdate(user);
    }

    private Set<Integer> getAccountIds(User user) {
        List<Subscription> subscriptions = user.getSubscriptions();

        return subscriptions.stream()
                .map(AbstractModel::getId)
                .collect(Collectors.toSet());
    }
}

