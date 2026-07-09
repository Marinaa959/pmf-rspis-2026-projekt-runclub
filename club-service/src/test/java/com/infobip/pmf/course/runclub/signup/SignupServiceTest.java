package com.infobip.pmf.course.runclub.signup;

import com.infobip.pmf.course.runclub.club.Club;
import com.infobip.pmf.course.runclub.club.ClubLevel;
import com.infobip.pmf.course.runclub.club.storage.ClubEntity;
import com.infobip.pmf.course.runclub.event.exception.EventNotFoundException;
import com.infobip.pmf.course.runclub.event.storage.EventEntity;
import com.infobip.pmf.course.runclub.event.storage.EventRepository;
import com.infobip.pmf.course.runclub.runner.Runner;
import com.infobip.pmf.course.runclub.runner.exception.RunnerNotFoundException;
import com.infobip.pmf.course.runclub.runner.storage.RunnerEntity;
import com.infobip.pmf.course.runclub.runner.storage.RunnerRepository;
import com.infobip.pmf.course.runclub.signup.exception.DuplicateSignupException;
import com.infobip.pmf.course.runclub.signup.storage.SignupEntity;
import com.infobip.pmf.course.runclub.signup.storage.SignupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class SignupServiceTest {

    @Mock
    private SignupRepository signupRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private RunnerRepository runnerRepository;

    @InjectMocks
    private SignupService service;

    private EventEntity event() {
        var club = ClubEntity.from(new Club(1L, "ZR", "Zagreb", ClubLevel.BEGINNER, null));
        return new EventEntity(club, "Jarun 10k", LocalDateTime.now(), 10000, 50);
    }

    private RunnerEntity runner() {
        return RunnerEntity.from(new Runner("marina-k", "Marina", "K", null, ClubLevel.INTERMEDIATE));
    }

    @Test
    void shouldSignUpRunnerForEvent() {
        given(eventRepository.findById(5L)).willReturn(Optional.of(event()));
        given(runnerRepository.findById("marina-k")).willReturn(Optional.of(runner()));
        given(signupRepository.existsByRunnerIdAndEventId("marina-k", 5L)).willReturn(false);
        given(signupRepository.save(any(SignupEntity.class))).willAnswer(inv -> inv.getArgument(0));

        var signup = service.signUp(5L, "marina-k");

        then(signup.runnerId()).isEqualTo("marina-k");
        BDDMockito.then(signupRepository).should().save(any(SignupEntity.class));
    }

    @Test
    void shouldThrowWhenEventDoesNotExist() {
        given(eventRepository.findById(5L)).willReturn(Optional.empty());

        thenExceptionOfType(EventNotFoundException.class)
                .isThrownBy(() -> service.signUp(5L, "marina-k"));

        BDDMockito.then(signupRepository).should(never()).save(any());
    }

    @Test
    void shouldThrowWhenRunnerDoesNotExist() {
        given(eventRepository.findById(5L)).willReturn(Optional.of(event()));
        given(runnerRepository.findById("ghost")).willReturn(Optional.empty());

        thenExceptionOfType(RunnerNotFoundException.class)
                .isThrownBy(() -> service.signUp(5L, "ghost"));
    }

    @Test
    void shouldThrowOnDuplicateSignup() {
        given(eventRepository.findById(5L)).willReturn(Optional.of(event()));
        given(runnerRepository.findById("marina-k")).willReturn(Optional.of(runner()));
        given(signupRepository.existsByRunnerIdAndEventId("marina-k", 5L)).willReturn(true);

        thenExceptionOfType(DuplicateSignupException.class)
                .isThrownBy(() -> service.signUp(5L, "marina-k"))
                .withMessage("Runner 'marina-k' is already signed up for event 5.");

        BDDMockito.then(signupRepository).should(never()).save(any());
    }
}
