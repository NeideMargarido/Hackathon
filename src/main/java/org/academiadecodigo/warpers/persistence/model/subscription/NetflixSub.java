package org.academiadecodigo.warpers.persistence.model.subscription;

import javax.persistence.Entity;

/**
 * A savings account model entity which requires a minimum balance
 * and can only be used for transferring money, not for debiting
 * @see Subscription
 * @see SubscriptionType#NETFLIX
 */
@Entity
public class NetflixSub extends Subscription {

    public static final double MIN_BALANCE = 100;

    /**
     * @see Subscription#getAccountType()
     */
    @Override
    public SubscriptionType getAccountType() {
        return SubscriptionType.NETFLIX;
    }

    /**
     * @see Subscription#canDebit(double)
     */
    @Override
    public boolean canDebit(double amount) {
        return super.canDebit(amount) && (getBalance() - amount) >= MIN_BALANCE;
    }

    /**
     * @see Subscription#canWithdraw()
     */
    @Override
    public boolean canWithdraw() {
        return false;
    }
}
