package org.academiadecodigo.warpers.persistence.model.subscription;

public class DisneyPlusSub extends Subscription {

    private String maxMembers;

    public String getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(String maxMembers) {
        this.maxMembers = maxMembers;
    }

    @Override
    public SubscriptionType getSubscriptionType() {
        return SubscriptionType.DISNEYPLUS;
    }
}
