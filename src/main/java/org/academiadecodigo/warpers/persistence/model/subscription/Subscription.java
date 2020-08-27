package org.academiadecodigo.warpers.persistence.model.subscription;

import org.academiadecodigo.warpers.persistence.model.AbstractModel;
import org.academiadecodigo.warpers.persistence.model.User;

import javax.persistence.*;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "subscription_type")
public abstract class Subscription extends AbstractModel {


    @ManyToOne
    private User user;

    private SubscriptionType subscriptionType;

    private String maxMembers;

    public User getUser() {
        return user;
    }


    public void setUser(User customer) {
        this.user = customer;
    }

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

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
