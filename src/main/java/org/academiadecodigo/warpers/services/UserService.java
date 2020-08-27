package org.academiadecodigo.warpers.services;

import org.academiadecodigo.warpers.exceptions.*;
import org.academiadecodigo.warpers.persistence.model.User;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;

import java.util.List;


public interface UserService {

    User get(Integer id);


    User save(User user);


    void delete(Integer id) throws AssociationExistsException, UserNotFoundException;


    List<User> list();

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
