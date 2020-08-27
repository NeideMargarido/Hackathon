package org.academiadecodigo.warpers.persistence.model.subscription;

public class AmazonPrimeSub extends Subscription{

    private String maxMembers;

    public void setMaxMembers(String maxMembers) {
        this.maxMembers = maxMembers;
    }


    public String getMaxMembers() {
        return maxMembers;
    }

    @Override
    public SubscriptionType getSubscriptionType() {
        return SubscriptionType.AMAZONPRIME;
    }
}
