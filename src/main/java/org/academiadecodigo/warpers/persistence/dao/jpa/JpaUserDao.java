package org.academiadecodigo.warpers.persistence.dao.jpa;

import org.academiadecodigo.warpers.persistence.dao.UserDao;
import org.academiadecodigo.warpers.persistence.model.User;
import org.springframework.stereotype.Repository;


@Repository
public class JpaUserDao extends GenericJpaDao<User> implements UserDao {


    public JpaUserDao() {
        super(User.class);
    }
}
