package org.academiadecodigo.warpers.persistence.dao.jpa;

import org.academiadecodigo.warpers.persistence.dao.SubscriptionDao;
import org.academiadecodigo.warpers.persistence.model.subscription.Subscription;
import org.springframework.stereotype.Repository;

/**
 * A JPA {@link SubscriptionDao} implementation
 */
@Repository
public class JpaSubscriptionDao extends GenericJpaDao<Subscription> implements SubscriptionDao {

    /**
     * @see GenericJpaDao#GenericJpaDao(Class)
     */
    public JpaSubscriptionDao() {
        super(Subscription.class);
    }
}
