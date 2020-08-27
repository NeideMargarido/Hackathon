package org.academiadecodigo.warpers.services;

import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;


public interface SubscriptionService {

    /**
     * Gets the account with the given id
     *
     * @param id the account id
     * @return the account
     */
    Subscription get(Integer id);


}
