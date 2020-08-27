package org.academiadecodigo.warpers.persistence.model.subscription;

import javax.persistence.Entity;


@Entity
public class SpotifySub extends Subscription {

private String maxMembers;

    public String getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(String maxMembers) {
        this.maxMembers = maxMembers;
    }

    @Override
    public String getSubscriptionType() {
        return SubscriptionType.SPOTIFY.toString();
    }



}
