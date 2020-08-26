package org.academiadecodigo.warpers.persistence.model;

import java.util.Arrays;
import java.util.List;

public enum SubscriptionType {


    NETFLIX,

    SPOTIFY;


    public static List<SubscriptionType> list() {
        return Arrays.asList(SubscriptionType.values());
    }}