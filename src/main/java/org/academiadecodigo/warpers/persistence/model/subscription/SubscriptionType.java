package org.academiadecodigo.warpers.persistence.model.subscription;

import java.util.Arrays;
import java.util.List;

public enum SubscriptionType {


    SPOTIFY,


    NETFLIX;


    public static List<SubscriptionType> list() {
        return Arrays.asList(SubscriptionType.values());
    }
}
