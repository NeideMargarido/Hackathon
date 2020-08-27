package org.academiadecodigo.warpers.persistence.model.subscription;

import javax.persistence.Entity;


@Entity
public class NetflixSub extends Subscription {

    private String maxMembers;

    public void setMaxMembers(String maxMembers) {
        this.maxMembers = maxMembers;
    }

    @Override
    public String getSubscriptionType() {
        return SubscriptionType.NETFLIX.toString();
    }

    public String getMaxMembers() {
        return getMaxMembers();
    }
}
