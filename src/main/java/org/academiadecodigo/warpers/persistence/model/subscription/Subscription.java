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

    public User getUser() {
        return user;
    }


    public void setUser(User customer) {
        this.user = customer;
    }


    public abstract SubscriptionType getSubscriptionType();
    public abstract String getMaxMembers();

    @Override
    public String toString() {
        return "Subscription{" +
                "subscription type=" + getSubscriptionType() +
                ", customerId=" + (user != null ? user.getId() : null) +
                "} " + super.toString();
    }
}
