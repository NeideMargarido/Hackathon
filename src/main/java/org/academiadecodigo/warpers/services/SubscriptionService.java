package org.academiadecodigo.warpers.services;

import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;

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
    Subscription get(Integer id);
}
