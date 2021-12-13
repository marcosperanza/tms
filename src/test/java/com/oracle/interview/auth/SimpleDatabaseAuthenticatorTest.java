package com.oracle.interview.auth;

import com.oracle.interview.db.UserRepository;
import com.oracle.interview.db.entity.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.basic.BasicCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SimpleDatabaseAuthenticatorTest {

    SimpleDatabaseAuthenticator authenticator;
    UserRepository userRepo;

    @BeforeEach
    void setUp() {
        User marcoUser = new User("marco", "secret", "ADMIN");
        userRepo = mock(UserRepository.class);
        Mockito.when(userRepo.getUser(eq("marco"))).thenReturn(Optional.of(marcoUser));
        authenticator = new SimpleDatabaseAuthenticator(userRepo);
    }

    @Test
    void authenticate() throws AuthenticationException {
        Optional<User> authenticate = authenticator.authenticate(new BasicCredentials("marco", "secret"));
        assertTrue(authenticate.isPresent());
        assertEquals("marco", authenticate.get().getUsername());

    }

    @Test
    void authenticateNotExistingUser() throws AuthenticationException {
        Optional<User> authenticate = authenticator.authenticate(new BasicCredentials("fake", ""));
        assertFalse(authenticate.isPresent());

    }

    @Test
    void authenticateWrongPass() throws AuthenticationException {
        Optional<User> authenticate = authenticator.authenticate(new BasicCredentials("marco", "fake"));
        assertFalse(authenticate.isPresent());

    }
}
