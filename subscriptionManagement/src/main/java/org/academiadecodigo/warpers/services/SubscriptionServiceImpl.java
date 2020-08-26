package org.academiadecodigo.warpers.services;

import org.academiadecodigo.warpers.exceptions.AccountNotFoundException;
import org.academiadecodigo.warpers.exceptions.CustomerNotFoundException;
import org.academiadecodigo.warpers.exceptions.TransactionInvalidException;
import org.academiadecodigo.warpers.persistence.dao.SubscriptionDao;
import org.academiadecodigo.warpers.persistence.dao.UserDao;
import org.academiadecodigo.warpers.persistence.model.Customer;
import org.academiadecodigo.warpers.persistence.model.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
     * Sets the customer data access object
     *
     * @param userDao the customer DAO to set
     */
    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * @see SubscriptionService#get(Integer)
     */
    @Override
    public Account get(Integer id) {
        return subscriptionDao.findById(id);
    }

    /**
     * @see SubscriptionService#deposit(Integer, Integer, double)
     */
    @Transactional
    @Override
    public void deposit(Integer id, Integer customerId, double amount)
            throws AccountNotFoundException, CustomerNotFoundException, TransactionInvalidException {

        Customer customer = Optional.ofNullable(userDao.findById(customerId))
                .orElseThrow(CustomerNotFoundException::new);

        Account account = Optional.ofNullable(subscriptionDao.findById(id))
                .orElseThrow(AccountNotFoundException::new);

        if (!account.getCustomer().getId().equals(customerId)) {
            throw new AccountNotFoundException();
        }

        if (!account.canCredit(amount)) {
            throw new TransactionInvalidException();
        }

        for (Account a : customer.getAccounts()) {
            if (a.getId().equals(id)) {
                a.credit(amount);
            }
        }

        userDao.saveOrUpdate(customer);
    }

    /**
     * @see SubscriptionService#withdraw(Integer, Integer, double)
     */
    @Transactional
    @Override
    public void withdraw(Integer id, Integer customerId, double amount)
            throws AccountNotFoundException, CustomerNotFoundException, TransactionInvalidException {

        Customer customer = Optional.ofNullable(userDao.findById(customerId))
                .orElseThrow(CustomerNotFoundException::new);

        Account account = Optional.ofNullable(subscriptionDao.findById(id))
                .orElseThrow(AccountNotFoundException::new);

        // in UI the user cannot click on Withdraw so this is here for safety because the user can bypass
        // the UI limitation easily
        if (!account.canWithdraw()) {
            throw new TransactionInvalidException();
        }

        if (!account.getCustomer().getId().equals(customerId)) {
            throw new AccountNotFoundException();
        }

        // make sure transaction can be performed
        if (!account.canDebit(amount)) {
            throw new TransactionInvalidException();
        }

        for (Account a : customer.getAccounts()) {
            if (a.getId().equals(id)) {
                a.debit(amount);
            }
        }

        userDao.saveOrUpdate(customer);
    }
}
