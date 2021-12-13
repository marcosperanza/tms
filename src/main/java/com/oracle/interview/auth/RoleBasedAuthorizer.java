package com.oracle.interview.auth;

import com.oracle.interview.db.entity.User;
import io.dropwizard.auth.Authorizer;

public class RoleBasedAuthorizer implements Authorizer<User> {

    @Override
    public boolean authorize(User user, String role) {
        return user.getRole() != null && user.getRole().equals(role);
    }
}
