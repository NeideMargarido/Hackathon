package org.academiadecodigo.warpers.factories;

import org.academiadecodigo.warpers.errors.ErrorMessage;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;
import org.academiadecodigo.warpers.persistence.model.subscription.SubscriptionType;
import org.academiadecodigo.warpers.persistence.model.subscription.SpotifySub;
import org.academiadecodigo.warpers.persistence.model.subscription.NetflixSub;
import org.springframework.stereotype.Component;

/**
 * A factory for creating accounts of different types
 */
@Component
public class SubscriptionFactory {

    /**
     * Creates a new {@link Subscription}
     *
     * @param subscriptionType the account type
     * @return the new account
     */
    public Subscription createAccount(SubscriptionType subscriptionType) {

        Subscription newSubscription;

        switch (subscriptionType) {
            case SPOTIFY:
                newSubscription = new SpotifySub();
                break;
            case NETFLIX:
                newSubscription = new NetflixSub();
                break;
            default:
                throw new IllegalArgumentException(ErrorMessage.TRANSACTION_INVALID);
        }

        return newSubscription;
    }
}
