package org.academiadecodigo.warpers.persistence.model.subscription;

import java.util.Arrays;
import java.util.List;

/**
 * The possible {@link Subscription} types
 */
public enum SubscriptionType {

    /**
     * @see SpotifySub
     */
    SPOTIFY,

    /**
     * @see NetflixSub
     */
    NETFLIX;

    /**
     * Lists the account types
     *
     * @return the list of account types
     */
    public static List<SubscriptionType> list() {
        return Arrays.asList(SubscriptionType.values());
    }
}
