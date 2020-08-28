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

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }


    @Autowired
    public void setSubscriptionDao(SubscriptionDao subscriptionDao) {
        this.subscriptionDao = subscriptionDao;
    }


    @Override
    public User get(Integer id) {
        return userDao.findById(id);
    }


    @Transactional
    @Override
    public User save(User user) {
        return userDao.saveOrUpdate(user);
    }


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

    @Override
    public List<User> list() {
        return userDao.findAll();
    }

    @Transactional
    @Override
    public Subscription addSubscription(Integer id, Subscription subscription) throws UserNotFoundException, TransactionInvalidException {

        User user = Optional.ofNullable(userDao.findById(id))
                .orElseThrow(UserNotFoundException::new);

        user.addAccount(subscription);
        userDao.saveOrUpdate(user);

        return user.getSubscriptions().get(user.getSubscriptions().size() - 1);
    }


    @Transactional
    @Override
    public void closeSubscription(Integer id, Integer subscriptionId)
            throws UserNotFoundException, AccountNotFoundException, TransactionInvalidException {

        User user = Optional.ofNullable(userDao.findById(id))
                .orElseThrow(UserNotFoundException::new);

        Subscription subscription = Optional.ofNullable(subscriptionDao.findById(subscriptionId))
                .orElseThrow(AccountNotFoundException::new);

        /*if (!subscription.getUser().getId().equals(id)) {
            throw new AccountNotFoundException();
        }*/

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

