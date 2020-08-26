package org.academiadecodigo.warpers.persistence.model;


public class NetflixSubscription extends Subscription {
    @Override
    public SubscriptionType getSubscriptionType() {
        return SubscriptionType.NETFLIX;
    }
}
