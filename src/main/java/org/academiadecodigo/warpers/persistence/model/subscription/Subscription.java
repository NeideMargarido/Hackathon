package org.academiadecodigo.warpers.persistence.model.subscription;

import org.academiadecodigo.warpers.persistence.model.AbstractModel;
import org.academiadecodigo.warpers.persistence.model.User;

import javax.persistence.*;


@Entity
@Table(name = "subscriptionType")
public abstract class Subscription extends AbstractModel {

    //@ManyToOne
    //private User user;

    private String subscriptionType;

    private String maxMembers;

    /*public User getUser() {
        return user;
    }

    public void setUser(User customer) {
        this.user = customer;
    }*/

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    /*public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }*/

    public String getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(String maxMembers) {
        this.maxMembers = maxMembers;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "subscription type=" + getSubscriptionType() +
                //", customerId=" + (user != null ? user.getId() : null) +
                "} " + super.toString();
    }
}
