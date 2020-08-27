package org.academiadecodigo.warpers.persistence.model.subscription;

public class HBOSub extends Subscription{
    private String maxMembers;

    public void setMaxMembers(String maxMembers) {
        this.maxMembers = maxMembers;
    }

    @Override
    public String getSubscriptionType() {
       return  SubscriptionType.HBO.toString();
    }


    public String getMaxMembers() {
        return maxMembers;
    }
}
