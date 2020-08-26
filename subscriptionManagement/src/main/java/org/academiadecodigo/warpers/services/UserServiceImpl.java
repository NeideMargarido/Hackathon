package org.academiadecodigo.warpers.services;

import org.academiadecodigo.warpers.exceptions.*;
import org.academiadecodigo.warpers.exceptions.*;
import org.academiadecodigo.warpers.persistence.dao.SubscriptionDao;
import org.academiadecodigo.warpers.persistence.dao.UserDao;
import org.academiadecodigo.warpers.persistence.dao.RecipientDao;
import org.academiadecodigo.warpers.persistence.model.AbstractModel;
import org.academiadecodigo.warpers.persistence.model.Customer;
import org.academiadecodigo.warpers.persistence.model.Recipient;
import org.academiadecodigo.warpers.persistence.model.account.Account;
import org.academiadecodigo.warpers.persistence.model.account.SavingsAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private RecipientDao recipientDao;
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
     * Sets the recipient data access object
     *
     * @param recipientDao the recipient DAO to set
     */
    @Autowired
    public void setRecipientDao(RecipientDao recipientDao) {
        this.recipientDao = recipientDao;
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
    public Customer get(Integer id) {
        return userDao.findById(id);
    }

    /**
     * @see UserService#getBalance(Integer)
     */
    @Override
    public double getBalance(Integer id) throws CustomerNotFoundException {

        Customer customer = Optional.ofNullable(userDao.findById(id))
                .orElseThrow(CustomerNotFoundException::new);

        return customer.getAccounts().stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }

    /**
     * @see UserService#save(Customer)
     */
    @Transactional
    @Override
    public Customer save(Customer customer) {
        return userDao.saveOrUpdate(customer);
    }

    /**
     * @see UserService#delete(Integer)
     */
    @Transactional
    @Override
    public void delete(Integer id) throws CustomerNotFoundException, AssociationExistsException {

        Customer customer = Optional.ofNullable(userDao.findById(id))
                .orElseThrow(CustomerNotFoundException::new);

        if (!customer.getAccounts().isEmpty()) {
            throw new AssociationExistsException();
        }

        userDao.delete(id);
    }

    /**
     * @see UserService#list()
     */
    @Override
    public List<Customer> list() {
        return userDao.findAll();
    }

    /**
     * @see UserService#listRecipients(Integer)
     */
    @Transactional(readOnly = true)
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
    @Transactional
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
    @Transactional
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
     * @see UserService#addAccount(Integer, Account)
     */
    @Transactional
    @Override
    public Account addAccount(Integer id, Account account) throws CustomerNotFoundException, TransactionInvalidException {

        Customer customer = Optional.ofNullable(userDao.findById(id))
                .orElseThrow(CustomerNotFoundException::new);

        if (!account.canWithdraw() &&
                account.getBalance() < SavingsAccount.MIN_BALANCE) {
            throw new TransactionInvalidException();
        }

        customer.addAccount(account);
        userDao.saveOrUpdate(customer);

        return customer.getAccounts().get(customer.getAccounts().size() - 1);
    }

    /**
     * @see UserService#closeAccount(Integer, Integer)
     */
    @Transactional
    @Override
    public void closeAccount(Integer id, Integer accountId)
            throws CustomerNotFoundException, AccountNotFoundException, TransactionInvalidException {

        Customer customer = Optional.ofNullable(userDao.findById(id))
                .orElseThrow(CustomerNotFoundException::new);

        Account account = Optional.ofNullable(subscriptionDao.findById(accountId))
                .orElseThrow(AccountNotFoundException::new);

        if (!account.getCustomer().getId().equals(id)) {
            throw new AccountNotFoundException();
        }

        //different from 0 in case we later decide that negative values are acceptable
        if (account.getBalance() != 0) {
            throw new TransactionInvalidException();
        }

        customer.removeAccount(account);
        userDao.saveOrUpdate(customer);
    }

    private Set<Integer> getAccountIds(Customer customer) {
        List<Account> accounts = customer.getAccounts();

        return accounts.stream()
                .map(AbstractModel::getId)
                .collect(Collectors.toSet());
    }
}

