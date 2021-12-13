package com.oracle.interview.auth;

import com.oracle.interview.db.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleBasedAuthorizerTest {

    RoleBasedAuthorizer underTest;

    @BeforeEach
    void setUp() {
        underTest = new RoleBasedAuthorizer();
    }

    @Test
    void name() {
        User u = new User("marco", "pass", "SUPER-SYAN");
        assertTrue(underTest.authorize(u, "SUPER-SYAN"));
        assertFalse(underTest.authorize(u, "NORMAL"));


    }
}
