package com.infobip.pmf.course.runclub.runner.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunnerRepository extends JpaRepository<RunnerEntity, String> {
}
