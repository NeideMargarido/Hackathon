package org.academiadecodigo.warpers.services;

import org.academiadecodigo.warpers.persistence.model.User;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;

import java.util.List;


public interface SubscriptionService {

    Subscription get(Integer id);


    List<Subscription> list ();

    Subscription save(Subscription subscription);

}
