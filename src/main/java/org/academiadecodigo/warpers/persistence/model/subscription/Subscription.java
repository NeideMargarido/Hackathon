package org.academiadecodigo.warpers.persistence.model.subscription;

import org.academiadecodigo.warpers.persistence.model.AbstractModel;
import org.academiadecodigo.warpers.persistence.model.User;

import javax.persistence.*;

/**
 * A generic account model entity to be used as a base for concrete types of accounts
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "subscription_type")
public abstract class Subscription extends AbstractModel {

    private double balance = 0;

    @ManyToOne
    private User user;

    public User getUser() {
        return user;
    }

    /**
     * Sets the account costumer
     *
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the account type
     *
     * @return the account type
     */
    public abstract SubscriptionType getAccountType();

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "Account{" +
                "balance=" + balance +
                ", userId=" + (user != null ? user.getId() : null) +
                "} " + super.toString();
    }
}
