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

    /**
     * Gets the account balance
     *
     * @return the account balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets the account balance
     *
     * @param balance the amount to set
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Gets the account costumer
     *
     * @return the customer
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the account costumer
     *
     * @param customer the customer to set
     */
    public void setUser(User customer) {
        this.user = customer;
    }

    /**
     * Gets the account type
     *
     * @return the account type
     */
    public abstract SubscriptionType getAccountType();

    /**
     * Credits account if possible
     *
     * @param amount the amount to credit
     * @see Subscription#credit(double)
     */
    public void credit(double amount) {
        if (canCredit(amount)) {
            balance += amount;
        }
    }

    /**
     * Debits the account if possible
     *
     * @param amount the amount to debit
     * @see Subscription#canDebit(double)
     */
    public void debit(double amount) {
        if (canDebit(amount)) {
            balance -= amount;
        }
    }

    /**
     * Checks if a specific amount can be credited on the account
     *
     * @param amount the amount to check
     * @return {@code true} if the account can be credited
     */
    public boolean canCredit(double amount) {
        return amount > 0;
    }

    /**
     * Checks if a specific amount can be debited from the account
     *
     * @param amount the amount to check
     * @return {@code true} if the account can be debited
     */
    public boolean canDebit(double amount) {
        return amount > 0 && amount <= balance;
    }

    /**
     * Checks if the account can be withdrawn
     *
     * @return {@code true} if withdraw can be done
     */
    public boolean canWithdraw() {
        return true;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "Account{" +
                "balance=" + balance +
                ", customerId=" + (user != null ? user.getId() : null) +
                "} " + super.toString();
    }
}
