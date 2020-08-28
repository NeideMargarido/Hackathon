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

    Subscription addSubscription(Integer id, Subscription subscription)
            throws UserNotFoundException, TransactionInvalidException;

    //void closeSubscription(Integer id, Integer subscriptionId)
            //throws UserNotFoundException, AccountNotFoundException, TransactionInvalidException;
}
