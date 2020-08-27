package org.academiadecodigo.warpers.services;

import org.academiadecodigo.warpers.exceptions.*;
import org.academiadecodigo.warpers.persistence.model.User;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;

import java.util.List;


public interface UserService {

    User get(Integer id);


    User save(User user);


    void delete(Integer id) throws AssociationExistsException, CustomerNotFoundException;


    List<User> list();

    Subscription addAccount(Integer id, Subscription subscription)
            throws CustomerNotFoundException, TransactionInvalidException;


    void closeAccount(Integer id, Integer accountId)
            throws CustomerNotFoundException, AccountNotFoundException, TransactionInvalidException;
}
