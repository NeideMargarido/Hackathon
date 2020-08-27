package org.academiadecodigo.warpers.services;

import org.academiadecodigo.warpers.exceptions.*;
import org.academiadecodigo.warpers.persistence.model.User;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;

import java.util.List;

/**
 * Common interface for user services, provides methods to manage users
 */
public interface UserService {

    /**
     * Gets the user with the given id
     *
     * @param id the user id
     * @return the user
     */
    User get(Integer id);

    /**
     * Saves a user
     *
     * @param user the user to save
     * @return the saved user
     */
    User save(User user);

    /**
     * Deletes the user
     *
     * @param id the user id
     * @throws UserNotFoundException
     * @throws AssociationExistsException
     */
    void delete(Integer id) throws AssociationExistsException, UserNotFoundException;

    /**
     * Gets a list of the users
     *
     * @return the users list
     */
    List<User> list();

    /**
     * Gets the list of user recipients
     *
     * @param id the user id
     * @return the list of recipients of the user
     * @throws UserNotFoundException
     */

    Subscription addAccount(Integer id, Subscription subscription)
            throws UserNotFoundException, TransactionInvalidException;

    /**
     * Closes an account from the user
     *
     * @param id        the user id
     * @param accountId the account id
     * @throws UserNotFoundException
     * @throws AccountNotFoundException
     * @throws TransactionInvalidException
     */
    void closeAccount(Integer id, Integer accountId)
            throws UserNotFoundException, AccountNotFoundException, TransactionInvalidException;
}
