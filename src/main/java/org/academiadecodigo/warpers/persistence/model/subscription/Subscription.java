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


    public double getBalance() {
        return balance;
    }


    public void setBalance(double balance) {
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }


    public void setUser(User customer) {
        this.user = customer;
    }


    public abstract SubscriptionType getAccountType();


    public void credit(double amount) {
        if (canCredit(amount)) {
            balance += amount;
        }
    }


    public void debit(double amount) {
        if (canDebit(amount)) {
            balance -= amount;
        }
    }


    public boolean canCredit(double amount) {
        return amount > 0;
    }


    public boolean canDebit(double amount) {
        return amount > 0 && amount <= balance;
    }


    public boolean canWithdraw() {
        return true;
    }


    @Override
    public String toString() {
        return "Account{" +
                "balance=" + balance +
                ", customerId=" + (user != null ? user.getId() : null) +
                "} " + super.toString();
    }
}
