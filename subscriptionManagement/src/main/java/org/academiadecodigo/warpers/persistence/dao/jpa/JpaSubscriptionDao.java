package org.academiadecodigo.warpers.persistence.dao.jpa;

import org.academiadecodigo.warpers.persistence.dao.SubscriptionDao;
import org.academiadecodigo.warpers.persistence.model.account.Account;
import org.springframework.stereotype.Repository;

/**
 * A JPA {@link SubscriptionDao} implementation
 */
@Repository
public class JpaSubscriptionDao extends GenericJpaDao<Account> implements SubscriptionDao {

    /**
     * @see GenericJpaDao#GenericJpaDao(Class)
     */
    public JpaSubscriptionDao() {
        super(Account.class);
    }
}
