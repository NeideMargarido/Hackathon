package org.academiadecodigo.warpers.services;

import org.academiadecodigo.warpers.persistence.dao.SubscriptionDao;
import org.academiadecodigo.warpers.persistence.dao.UserDao;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * An {@link SubscriptionService} implementation
 */
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private SubscriptionDao subscriptionDao;
    private UserDao userDao;

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
     * Sets the user data access object
     *
     * @param userDao the user DAO to set
     */
    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * @see SubscriptionService#get(Integer)
     */
    @Override
    public Subscription get(Integer id) {
        return subscriptionDao.findById(id);
    }
}
