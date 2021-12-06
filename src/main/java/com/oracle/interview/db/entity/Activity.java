package com.oracle.interview.db.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "activities")
@NamedQueries(
{
    @NamedQuery(
            name = "com.oracle.activity.findAll",
            query = "SELECT a FROM Activity a"
    )
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