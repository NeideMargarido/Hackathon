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
    public void delete(Integer id) throws CustomerNotFoundException, AssociationExistsException {

        User user = Optional.ofNullable(userDao.findById(id))
                .orElseThrow(CustomerNotFoundException::new);

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
    public Subscription addAccount(Integer id, Subscription subscription) throws CustomerNotFoundException, TransactionInvalidException {

        User user = Optional.ofNullable(userDao.findById(id))
                .orElseThrow(CustomerNotFoundException::new);

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
            throws CustomerNotFoundException, AccountNotFoundException, TransactionInvalidException {

        User user = Optional.ofNullable(userDao.findById(id))
                .orElseThrow(CustomerNotFoundException::new);

        Subscription subscription = Optional.ofNullable(subscriptionDao.findById(accountId))
                .orElseThrow(AccountNotFoundException::new);

        if (!subscription.getUser().getId().equals(id)) {
            throw new AccountNotFoundException();
        }

        //different from 0 in case we later decide that negative values are acceptable
        if (subscription.getBalance() != 0) {
            throw new TransactionInvalidException();
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

