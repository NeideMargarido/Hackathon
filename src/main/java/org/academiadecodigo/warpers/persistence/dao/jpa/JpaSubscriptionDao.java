package org.academiadecodigo.warpers.persistence.dao.jpa;

import org.academiadecodigo.warpers.persistence.dao.SubscriptionDao;
import org.academiadecodigo.warpers.persistence.model.Subs;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;
import org.springframework.stereotype.Repository;


@Repository
public class JpaSubscriptionDao extends GenericJpaDao<Subs> implements SubscriptionDao {


    public JpaSubscriptionDao() {
        super(Subs.class);
    }
}
