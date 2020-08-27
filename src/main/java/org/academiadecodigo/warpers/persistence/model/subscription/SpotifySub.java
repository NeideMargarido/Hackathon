package org.academiadecodigo.warpers.persistence.model.subscription;

import javax.persistence.Entity;


@Entity
public class SpotifySub extends Subscription {


    @Override
    public SubscriptionType getAccountType() {
        return SubscriptionType.SPOTIFY;
    }
}
