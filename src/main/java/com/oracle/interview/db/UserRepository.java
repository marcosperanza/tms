package com.oracle.interview.db;


import com.oracle.interview.db.entity.User;

import java.util.Optional;

/**
 * The storage APIs for manipulate the activities list
 */
public interface UserRepository {

    Optional<User> getUser(String name);

}
