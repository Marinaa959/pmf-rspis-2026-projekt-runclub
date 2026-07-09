package com.infobip.pmf.course.runclub.signup.storage;

import com.infobip.pmf.course.runclub.event.storage.EventEntity;
import com.infobip.pmf.course.runclub.runner.storage.RunnerEntity;
import com.infobip.pmf.course.runclub.signup.Signup;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "signup", uniqueConstraints =
        @UniqueConstraint(columnNames = {"runner_id", "event_id"}))
public class SignupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)  // sprječavamo dvostruku prijavu
    @JoinColumn(name = "runner_id", nullable = false)
    private RunnerEntity runner;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected SignupEntity() {
    }

    public SignupEntity(RunnerEntity runner, EventEntity event, LocalDateTime createdAt) {
        this.runner = runner;
        this.event = event;
        this.createdAt = createdAt;
    }

    public Signup asSignup() {
        return new Signup(id, runner.getId(), event.getId(), createdAt);
    }

    public Long getId() { return id; }
    public RunnerEntity getRunner() { return runner; }
    public EventEntity getEvent() { return event; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
