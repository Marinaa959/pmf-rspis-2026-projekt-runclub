package com.infobip.pmf.course.runclub.event.storage;

import com.infobip.pmf.course.runclub.club.storage.ClubEntity;
import com.infobip.pmf.course.runclub.event.Event;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "event")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "club_id", nullable = false)
    private ClubEntity club;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private double distanceMeters;

    @Column(nullable = false)
    private double elevationGainMeters;

    protected EventEntity() {
    }

    public EventEntity(ClubEntity club, String name, LocalDateTime startTime,
                       double distanceMeters, double elevationGainMeters) {
        this.club = club;
        this.name = name;
        this.startTime = startTime;
        this.distanceMeters = distanceMeters;
        this.elevationGainMeters = elevationGainMeters;
    }

    public Event asEvent() {
        return new Event(id, club.getId(), name, startTime, distanceMeters, elevationGainMeters);
    }

    public Long getId() { return id; }
    public ClubEntity getClub() { return club; }
    public String getName() { return name; }
    public LocalDateTime getStartTime() { return startTime; }
    public double getDistanceMeters() { return distanceMeters; }
    public double getElevationGainMeters() { return elevationGainMeters; }
}
