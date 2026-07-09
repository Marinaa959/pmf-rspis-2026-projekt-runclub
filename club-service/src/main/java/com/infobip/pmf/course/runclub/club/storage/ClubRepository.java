package com.infobip.pmf.course.runclub.club.storage;

import com.infobip.pmf.course.runclub.club.ClubLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Implementaciju i SQL generira Spring iz naziva metode
@Repository
public interface ClubRepository extends JpaRepository<ClubEntity, Long> {

    boolean existsByName(String name);

    List<ClubEntity> findByLevel(ClubLevel level);
}
