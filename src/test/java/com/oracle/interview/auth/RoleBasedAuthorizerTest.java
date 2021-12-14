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
    void authorizeUsersBasedOnRole() {
        User u = new User("marco", "pass", "SUPER-SYAN");
        assertTrue(underTest.authorize(u, "SUPER-SYAN"));
    }

    @Test
    void unAuthorizeUsersWithDifferentRoles() {
        User u = new User("marco", "pass", "SUPER-SYAN");
        assertFalse(underTest.authorize(u, "NORMAL"));
    }
}
