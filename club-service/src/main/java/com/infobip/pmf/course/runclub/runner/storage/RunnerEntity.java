package com.infobip.pmf.course.runclub.runner.storage;

import com.infobip.pmf.course.runclub.club.ClubLevel;
import com.infobip.pmf.course.runclub.runner.Runner;
import jakarta.persistence.*;

@Entity
@Table(name = "runner")
public class RunnerEntity {

    @Id
    @Column(length = 30)
    private String id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClubLevel preferredLevel;

    protected RunnerEntity() {
    }

    public static RunnerEntity from(Runner runner) {
        var entity = new RunnerEntity();
        entity.id = runner.id();
        entity.firstName = runner.firstName();
        entity.lastName = runner.lastName();
        entity.email = runner.email();
        entity.preferredLevel = runner.preferredLevel();
        return entity;
    }

    public Runner asRunner() {
        return new Runner(id, firstName, lastName, email, preferredLevel);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public ClubLevel getPreferredLevel() { return preferredLevel; }
    public void setPreferredLevel(ClubLevel preferredLevel) { this.preferredLevel = preferredLevel; }
}
