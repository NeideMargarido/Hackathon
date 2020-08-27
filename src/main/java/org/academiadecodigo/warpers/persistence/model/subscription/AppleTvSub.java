package org.academiadecodigo.warpers.persistence.model.subscription;

public class AppleTvSub extends Subscription {

    private String maxMembers;

    public String getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(String maxMembers) {
        this.maxMembers = maxMembers;
    }

    @Override
    public String getSubscriptionType() {
        return SubscriptionType.APPLETV_APPLEMUSIC.toString();
    }
}
