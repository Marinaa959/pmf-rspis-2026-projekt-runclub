package com.infobip.pmf.course.runclub.signup.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignupRepository extends JpaRepository<SignupEntity, Long> {

    boolean existsByRunnerIdAndEventId(String runnerId, Long eventId);

    List<SignupEntity> findByEventId(Long eventId);

    List<SignupEntity> findByRunnerId(String runnerId);
}
