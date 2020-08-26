package org.academiadecodigo.warpers.persistence.model;

public class SotifySubscription extends Subscription{

    @Override
    public SubscriptionType getSubscriptionType() {
        return SubscriptionType.SPOTIFY;
    }
}

