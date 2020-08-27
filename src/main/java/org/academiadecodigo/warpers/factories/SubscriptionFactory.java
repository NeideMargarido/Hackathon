package org.academiadecodigo.warpers.factories;

import org.academiadecodigo.warpers.errors.ErrorMessage;
import org.academiadecodigo.warpers.persistence.model.subscription.*;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionFactory {


    public Subscription createSubscription(SubscriptionType subscriptionType) {

        Subscription newSubscription;

        switch (subscriptionType) {
            case SPOTIFY:
                newSubscription = new SpotifySub();
                break;
            case NETFLIX:
                newSubscription = new NetflixSub();
                break;
            case HBO:
                newSubscription = new HBOSub();
                break;
            case AMAZONPRIME:
                newSubscription = new AmazonPrimeSub();
                break;
            case APPLETV_APPLEMUSIC:
                newSubscription = new AppleTvSub();
            case DISNEYPLUS:
                newSubscription = new DisneyPlusSub();
                break;
            default:
                throw new IllegalArgumentException(ErrorMessage.TRANSACTION_INVALID);
        }

        return newSubscription;
    }
}
