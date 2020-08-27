package org.academiadecodigo.warpers.persistence.model.subscription;

import javax.persistence.Entity;


@Entity
public class NetflixSub extends Subscription {

    public static final double MIN_BALANCE = 100;


    @Override
    public SubscriptionType getAccountType() {
        return SubscriptionType.NETFLIX;
    }


    @Override
    public boolean canDebit(double amount) {
        return super.canDebit(amount) && (getBalance() - amount) >= MIN_BALANCE;
    }


    @Override
    public boolean canWithdraw() {
        return false;
    }
}
