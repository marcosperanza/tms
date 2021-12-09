package com.oracle.interview.db.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;


/**
 * {@code Activity} POJO describes that single activity that will be stored int the persistence and sent to the UI
 */

@Entity
@Table(name = "activities")
@NamedQueries({
    @NamedQuery(name = "com.oracle.activity.findAll", query = "SELECT a FROM Activity a"),
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

    public Activity() {
    }

    public Activity(String description, long date, boolean done) {
        this.description = description;
        this.date = date;
        this.done = done;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity that = (Activity) o;
        return date == that.date && done == that.done && Objects.equals(id, that.id) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, date, done);
    }
}
