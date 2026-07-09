package com.infobip.pmf.course.runclub.club.storage;

import com.infobip.pmf.course.runclub.club.Club;
import com.infobip.pmf.course.runclub.club.ClubLevel;
import jakarta.persistence.*;

@Entity
@Table(name = "club")
public class ClubEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id dodjeljuje baza
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClubLevel level;

    @Column(length = 2000)
    private String description;

    protected ClubEntity() {
    }

    public static ClubEntity from(Club club) {
        var entity = new ClubEntity();
        entity.id = club.id();
        entity.name = club.name();
        entity.city = club.city();
        entity.level = club.level();
        entity.description = club.description();
        return entity;
    }

    public Club asClub() {
        return new Club(id, name, city, level, description);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public ClubLevel getLevel() { return level; }
    public void setLevel(ClubLevel level) { this.level = level; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
