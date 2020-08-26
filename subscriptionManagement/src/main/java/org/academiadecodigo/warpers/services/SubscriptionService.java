package org.academiadecodigo.warpers.services;

import org.academiadecodigo.warpers.exceptions.AccountNotFoundException;
import org.academiadecodigo.warpers.exceptions.CustomerNotFoundException;
import org.academiadecodigo.warpers.exceptions.TransactionInvalidException;
import org.academiadecodigo.warpers.persistence.model.account.Account;

/**
 * Common interface for account services, provides methods to manage accounts and perform account transactions
 */
public interface SubscriptionService {

    /**
     * Gets the account with the given id
     *
     * @param id the account id
     * @return the account
     */
    Account get(Integer id);

    /**
     * Performs an {@link Account} deposit
     *
     * @param id         the account id
     * @param customerId the customer id
     * @param amount     the amount to deposit
     * @throws AccountNotFoundException
     * @throws CustomerNotFoundException
     * @throws TransactionInvalidException
     */
    void deposit(Integer id, Integer customerId, double amount)
            throws AccountNotFoundException, CustomerNotFoundException, TransactionInvalidException;

    /**
     * Perform an {@link Account} withdrawal
     *
     * @param id         the account id
     * @param customerId the customer id
     * @param amount     the amount to withdraw
     * @throws AccountNotFoundException
     * @throws CustomerNotFoundException
     * @throws TransactionInvalidException
     */
    void withdraw(Integer id, Integer customerId, double amount)
            throws AccountNotFoundException, CustomerNotFoundException, TransactionInvalidException;
}
