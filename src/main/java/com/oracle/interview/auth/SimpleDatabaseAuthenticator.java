package com.oracle.interview.auth;

import com.oracle.interview.db.UserRepository;
import com.oracle.interview.db.entity.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

/**
 * A simple database authenticator.
 * check if the user is in the Database, if yes returns  the user instance, if not return a guest user
 */
public class SimpleDatabaseAuthenticator implements Authenticator<BasicCredentials, User> {

    public static final User GUEST_USER = new User("guest", null, "BASIC");

    private final UserRepository daoUser;

    public SimpleDatabaseAuthenticator(UserRepository daoUser) {
        this.daoUser = daoUser;
    }

    @Override
    @UnitOfWork
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        Optional<User> user = daoUser.getUser(credentials.getUsername());
        if (user.isPresent() &&  credentials.getPassword().equals(user.get().getPassword())) {
            return Optional.of(new User(credentials.getUsername(), user.get().getPassword(), user.get().getRole()));
        }
        return Optional.empty();
    }
}
