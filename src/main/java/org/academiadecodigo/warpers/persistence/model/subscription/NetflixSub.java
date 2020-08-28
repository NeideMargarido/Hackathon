package org.academiadecodigo.warpers.persistence.model.subscription;

import javax.persistence.Entity;


@Entity
public class NetflixSub extends Subscription {

    private String maxMembers;

    public void setMaxMembers(String maxMembers) {
        this.maxMembers = maxMembers;
    }

    @Override
    public SubscriptionType getSubscriptionType() {
        return SubscriptionType.NETFLIX;
    }

    public String getMaxMembers() {
        return getMaxMembers();
    }
}
