package org.academiadecodigo.warpers.persistence.dao.jpa;

import org.academiadecodigo.warpers.persistence.dao.UserDao;
import org.academiadecodigo.warpers.persistence.model.Customer;
import org.springframework.stereotype.Repository;

/**
 * A JPA {@link UserDao} implementation
 */
@Repository
public class JpaUserDao extends GenericJpaDao<Customer> implements UserDao {

    /**
     * @see GenericJpaDao#GenericJpaDao(Class)
     */
    public JpaUserDao() {
        super(Customer.class);
    }
}
