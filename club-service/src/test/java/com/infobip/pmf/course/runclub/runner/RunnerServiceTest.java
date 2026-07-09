package com.infobip.pmf.course.runclub.runner;

import com.infobip.pmf.course.runclub.club.ClubLevel;
import com.infobip.pmf.course.runclub.runner.exception.DuplicateRunnerException;
import com.infobip.pmf.course.runclub.runner.exception.ForbiddenRunnerUpdateException;
import com.infobip.pmf.course.runclub.runner.exception.RunnerNotFoundException;
import com.infobip.pmf.course.runclub.runner.storage.RunnerEntity;
import com.infobip.pmf.course.runclub.runner.storage.RunnerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class RunnerServiceTest {

    @Mock
    private RunnerRepository repository;

    @InjectMocks
    private RunnerService service;

    private final Runner runner = new Runner("marina-k", "Marina", "K", "m@example.com", ClubLevel.INTERMEDIATE);

    @Test
    void shouldGetOneRunner() {
        given(repository.findById("marina-k")).willReturn(Optional.of(RunnerEntity.from(runner)));

        var found = service.getOne("marina-k");

        then(found).isEqualTo(runner);
    }

    @Test
    void shouldThrowWhenRunnerNotFound() {
        given(repository.findById("ghost")).willReturn(Optional.empty());

        thenExceptionOfType(RunnerNotFoundException.class)
                .isThrownBy(() -> service.getOne("ghost"))
                .withMessage("Runner with id 'ghost' does not exist.");
    }

    @Test
    void shouldCreateRunner() {
        given(repository.existsById("marina-k")).willReturn(false);
        given(repository.save(any(RunnerEntity.class))).willAnswer(inv -> inv.getArgument(0));

        var created = service.create(runner);

        then(created).isEqualTo(runner);
    }

    @Test
    void shouldThrowWhenCreatingDuplicateRunner() {
        given(repository.existsById("marina-k")).willReturn(true);

        thenExceptionOfType(DuplicateRunnerException.class)
                .isThrownBy(() -> service.create(runner))
                .withMessage("Runner with id 'marina-k' already exists.");

        BDDMockito.then(repository).should(never()).save(any());
    }

    @Test
    void shouldThrowWhenChangingRunnerId() {
        // pokušaj promjene id-ja kroz update
        thenExceptionOfType(ForbiddenRunnerUpdateException.class)
                .isThrownBy(() -> service.update("other-id", runner))
                .withMessage("Updating runner's id is not allowed.");

        BDDMockito.then(repository).should(never()).save(any());
    }

    @Test
    void shouldUpdateRunner() {
        given(repository.existsById("marina-k")).willReturn(true);
        given(repository.save(any(RunnerEntity.class))).willAnswer(inv -> inv.getArgument(0));

        var updated = service.update("marina-k", runner);

        then(updated).isEqualTo(runner);
    }
}
