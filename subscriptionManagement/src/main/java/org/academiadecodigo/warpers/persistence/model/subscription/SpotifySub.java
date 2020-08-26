package org.academiadecodigo.warpers.persistence.model.subscription;

import javax.persistence.Entity;

/**
 * A checking account with no restrictions
 * @see Subscription
 * @see SubscriptionType#SPOTIFY
 */
@Entity
public class SpotifySub extends Subscription {

    /**
     * @see Subscription#getAccountType()
     */
    @Override
    public SubscriptionType getAccountType() {
        return SubscriptionType.SPOTIFY;
    }
}
