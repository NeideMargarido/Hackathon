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
     * Sets the customer data access object
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


    /*@Override
    public double getBalance(Integer id) throws CustomerNotFoundException {

        /*Customer customer = Optional.ofNullable(userDao.findById(id))
                .orElseThrow(CustomerNotFoundException::new);*/

        /*return 0;customer.getAccounts().stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }*/


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
    public void delete(Integer id) throws CustomerNotFoundException, AssociationExistsException {

        User user = Optional.ofNullable(userDao.findById(id))
                .orElseThrow(CustomerNotFoundException::new);

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


    /*@Transactional(readOnly = true)
    @Override
    public List<Recipient> listRecipients(Integer id) throws CustomerNotFoundException {

        // check then act logic requires transaction,
        // event if read only

        Customer customer = Optional.ofNullable(userDao.findById(id))
                .orElseThrow(CustomerNotFoundException::new);

        return new ArrayList<>(customer.getRecipients());
    }

    /**
     * @see UserService#addRecipient(Integer, Recipient)
     */
   /* @Transactional
    @Override
    public Recipient addRecipient(Integer id, Recipient recipient) throws CustomerNotFoundException, AccountNotFoundException {

        Customer customer = Optional.ofNullable(userDao.findById(id))
                .orElseThrow(CustomerNotFoundException::new);

        if (subscriptionDao.findById(recipient.getAccountNumber()) == null ||
                getAccountIds(customer).contains(recipient.getAccountNumber())) {
            throw new AccountNotFoundException();
        }

        if (recipient.getId() == null) {
            customer.addRecipient(recipient);
            userDao.saveOrUpdate(customer);
        } else {
            recipientDao.saveOrUpdate(recipient);
        }
        return customer.getRecipients().get(customer.getRecipients().size() - 1);
    }

    /**
     * @see UserService#removeRecipient(Integer, Integer)
     */
   /* @Transactional
    @Override
    public void removeRecipient(Integer id, Integer recipientId) throws CustomerNotFoundException, RecipientNotFoundException {

        Customer customer = Optional.ofNullable(userDao.findById(id))
                .orElseThrow(CustomerNotFoundException::new);

        Recipient recipient = Optional.ofNullable(recipientDao.findById(recipientId))
                .orElseThrow(RecipientNotFoundException::new);

        if (!customer.getRecipients().contains(recipient)) {
            throw new RecipientNotFoundException();
        }

        customer.removeRecipient(recipient);
        userDao.saveOrUpdate(customer);
    }

    /**
     * @see UserService#addAccount(Integer, Subscription)
     */
    @Transactional
    @Override
    public Subscription addAccount(Integer id, Subscription subscription) throws CustomerNotFoundException, TransactionInvalidException {

        User user = Optional.ofNullable(userDao.findById(id))
                .orElseThrow(CustomerNotFoundException::new);

        /*if (!subscription.canWithdraw() &&
                subscription.getBalance() < NetflixSub.MIN_BALANCE) {
            throw new TransactionInvalidException();
        }*/

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

    private Set<Integer> getAccountIds(User customer) {
        List<Subscription> subscriptions = customer.getSubscriptions();

        return subscriptions.stream()
                .map(AbstractModel::getId)
                .collect(Collectors.toSet());
    }
}

