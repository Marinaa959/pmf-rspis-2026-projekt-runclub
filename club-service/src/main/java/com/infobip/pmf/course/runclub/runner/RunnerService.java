package com.infobip.pmf.course.runclub.runner;

import com.infobip.pmf.course.runclub.runner.exception.DuplicateRunnerException;
import com.infobip.pmf.course.runclub.runner.exception.ForbiddenRunnerUpdateException;
import com.infobip.pmf.course.runclub.runner.exception.RunnerNotFoundException;
import com.infobip.pmf.course.runclub.runner.storage.RunnerEntity;
import com.infobip.pmf.course.runclub.runner.storage.RunnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RunnerService {

    private final RunnerRepository repository;

    public RunnerService(RunnerRepository repository) {
        this.repository = repository;
    }

    public List<Runner> getAll() {
        return repository.findAll().stream()
                .map(RunnerEntity::asRunner)
                .toList();
    }

    public Runner getOne(String id) {
        return repository.findById(id)
                .map(RunnerEntity::asRunner)
                .orElseThrow(() -> new RunnerNotFoundException(id));
    }

    public Runner create(Runner runner) {
        if (repository.existsById(runner.id())) {
            throw new DuplicateRunnerException(runner.id());
        }
        return repository.save(RunnerEntity.from(runner)).asRunner();
    }

    public Runner update(String id, Runner runner) {
        if (!Objects.equals(id, runner.id())) {
            throw new ForbiddenRunnerUpdateException(); // id se ne mijenja (kao JMBAG)
        }
        if (!repository.existsById(id)) {
            throw new RunnerNotFoundException(id);
        }
        return repository.save(RunnerEntity.from(runner)).asRunner();
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new RunnerNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
