package org.academiadecodigo.warpers.services;

import org.academiadecodigo.warpers.exceptions.*;
import org.academiadecodigo.warpers.persistence.model.User;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;

import java.util.List;

/**
 * Common interface for customer services, provides methods to manage customers
 */
public interface UserService {

    /**
     * Gets the customer with the given id
     *
     * @param id the customer id
     * @return the customer
     */
    User get(Integer id);

    /**
     * Gets the balance of the customer
     *
     * @param id the customer id
     * @return the balance of the customer with the given id
     * @throws CustomerNotFoundException
     */
    //double getBalance(Integer id) throws CustomerNotFoundException;

    /**
     * Saves a user
     *
     * @param user the user to save
     * @return the saved custoemr
     */
    User save(User user);

    /**
     * Deletes the customer
     *
     * @param id the customer id
     * @throws CustomerNotFoundException
     * @throws AssociationExistsException
     */
    void delete(Integer id) throws AssociationExistsException, CustomerNotFoundException;

    /**
     * Gets a list of the customers
     *
     * @return the customers list
     */
    List<User> list();

    /**
     * Gets the list of customer recipients
     *
     * @param id the customer id
     * @return the list of recipients of the customer
     * @throws CustomerNotFoundException
     */
    //List<Recipient> listRecipients(Integer id) throws CustomerNotFoundException;
    /*Recipient addRecipient(Integer id, Recipient recipient)
            throws CustomerNotFoundException, AccountNotFoundException;

    /**
     * Removes a recipient from the customer
     *
     * @param id          the customer id
     * @param recipientId the recipient id
     * @throws CustomerNotFoundException
     * @throws AccountNotFoundException
     * @throws RecipientNotFoundException
     */
    /*void removeRecipient(Integer id, Integer recipientId)
            throws CustomerNotFoundException, AccountNotFoundException, RecipientNotFoundException;

    /**
     * Adds an subscriptions to a customer
     *
     * @param id      the customer id
     * @param subscription the subscriptions
     * @throws CustomerNotFoundException
     * @throws TransactionInvalidException
     */
    Subscription addAccount(Integer id, Subscription subscription)
            throws CustomerNotFoundException, TransactionInvalidException;

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


    /**
     * Closes an account from the customer
     *
     * @param id        the customer id
     * @param accountId the account id
     * @throws CustomerNotFoundException
     * @throws AccountNotFoundException
     * @throws TransactionInvalidException
     */
    void closeAccount(Integer id, Integer accountId)
            throws CustomerNotFoundException, AccountNotFoundException, TransactionInvalidException;
}
