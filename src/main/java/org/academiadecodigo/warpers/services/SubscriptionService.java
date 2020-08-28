package org.academiadecodigo.warpers.services;

import org.academiadecodigo.warpers.persistence.model.Subs;
import org.academiadecodigo.warpers.persistence.model.User;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;

import java.util.List;


public interface SubscriptionService {

    //Subscription get(Integer id);

    Subs get(Integer id);


    List<Subs> list ();

    Subs save(Subs subscription);

}
