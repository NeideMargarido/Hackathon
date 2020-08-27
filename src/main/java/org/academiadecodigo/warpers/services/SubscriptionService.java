package org.academiadecodigo.warpers.services;

import org.academiadecodigo.warpers.exceptions.AccountNotFoundException;
import org.academiadecodigo.warpers.exceptions.CustomerNotFoundException;
import org.academiadecodigo.warpers.exceptions.TransactionInvalidException;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;


public interface SubscriptionService {

    Subscription get(Integer id);


}
