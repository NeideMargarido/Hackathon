package org.academiadecodigo.warpers.persistence.dao.jpa;

import org.academiadecodigo.warpers.persistence.dao.RecipientDao;
import org.academiadecodigo.warpers.persistence.model.Recipient;
import org.springframework.stereotype.Repository;

/**
 * A JPA {@link RecipientDao} implementation
 */
@Repository
public class JpaRecipientDao extends GenericJpaDao<Recipient> implements RecipientDao {

    /**
     * @see GenericJpaDao#GenericJpaDao(Class)
     */
    public JpaRecipientDao() {
        super(Recipient.class);
    }
}
