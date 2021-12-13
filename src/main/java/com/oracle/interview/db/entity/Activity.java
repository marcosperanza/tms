package com.oracle.interview.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;


/**
 * {@code Activity} POJO describes that single activity that will be stored int the persistence and sent to the UI
 */

@Entity
@Table(name = "activities")
@NamedQueries({

    @NamedQuery(name = "com.oracle.activity.findAll", query = "SELECT a FROM Activity a where a.user = :user"),

    @NamedQuery(name = "com.oracle.activity.update", query = "update Activity set done = :done where id = :id"),
    @NamedQuery(name = "com.oracle.activity.deleteAll", query = "delete from Activity "),
    @NamedQuery(name = "com.oracle.activity.deleteById", query = "delete from Activity where id = :id")
})
public class Activity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(nullable = false, unique = true)
    private String id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "date", nullable = false)
    private long date;


    @Column(name = "done")
    private boolean done;

    @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name = "user")
    @JsonIgnore
    private User user;

    public Activity() {
    }

    public Activity(String description, long date, boolean done) {
        this.description = description;
        this.date = date;
        this.done = done;
    }

    public Activity(String description, long date, boolean done, User user) {
        this.description = description;
        this.date = date;
        this.done = done;
        this.user = user;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return date == activity.date && done == activity.done && Objects.equals(id, activity.id) && Objects.equals(description, activity.description) && Objects.equals(user, activity.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, date, done, user);
    }
}
