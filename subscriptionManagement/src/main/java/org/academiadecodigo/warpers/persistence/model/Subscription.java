package org.academiadecodigo.warpers.persistence.model;

import javax.persistence.*;



@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "subscription_type")
public abstract class Subscription {
    @ManyToOne
    private User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user=user;
    }


    public abstract SubscriptionType getSubscriptionType();


}
