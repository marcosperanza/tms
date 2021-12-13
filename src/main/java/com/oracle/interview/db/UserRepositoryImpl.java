package com.oracle.interview.db;

import com.oracle.interview.db.entity.User;
import io.dropwizard.hibernate.AbstractDAO;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;

public class UserRepositoryImpl extends AbstractDAO<User> implements UserRepository  {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    @Override
    public Optional<User> getUser(String name) {
        return Optional.ofNullable(get(name));
    }
}
