package com.oracle.interview.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.security.auth.Subject;
import java.security.Principal;
import java.util.Objects;
import java.util.Set;


/**
 * {@code User} POJO describes the users
 */

@Entity
@Table(name = "users")
public class User implements java.security.Principal {

    @Id
    @Column(nullable = false, unique = true, name = "username")
    private String username;

    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "role", nullable = true)
    private String role;

    public User(String name) {
        this.username = name;
        this.role = null;
    }

    public User(String name, String password, String role) {
        this.username = name;
        this.password = password;
        this.role = role;
    }

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
}
