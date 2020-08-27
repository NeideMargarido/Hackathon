package org.academiadecodigo.warpers.persistence.model.subscription;

import javax.persistence.Entity;


@Entity
public class NetflixSub extends Subscription {

    public static final double MIN_BALANCE = 100;

    @Override
    public SubscriptionType getAccountType() {
        return SubscriptionType.NETFLIX;
    }
}
