package com.infobip.pmf.course.runclub.signup;

import com.infobip.pmf.course.runclub.event.exception.EventNotFoundException;
import com.infobip.pmf.course.runclub.event.storage.EventRepository;
import com.infobip.pmf.course.runclub.runner.exception.RunnerNotFoundException;
import com.infobip.pmf.course.runclub.runner.storage.RunnerRepository;
import com.infobip.pmf.course.runclub.signup.exception.DuplicateSignupException;
import com.infobip.pmf.course.runclub.signup.storage.SignupEntity;
import com.infobip.pmf.course.runclub.signup.storage.SignupRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SignupService {

    private final SignupRepository signupRepository;
    private final EventRepository eventRepository;
    private final RunnerRepository runnerRepository;

    public SignupService(SignupRepository signupRepository,
                         EventRepository eventRepository,
                         RunnerRepository runnerRepository) {
        this.signupRepository = signupRepository;
        this.eventRepository = eventRepository;
        this.runnerRepository = runnerRepository;
    }

    // provjeri da event i trkac postoje, spriječi duplikat
    public Signup signUp(Long eventId, String runnerId) {
        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        var runner = runnerRepository.findById(runnerId)
                .orElseThrow(() -> new RunnerNotFoundException(runnerId));

        if (signupRepository.existsByRunnerIdAndEventId(runnerId, eventId)) {
            throw new DuplicateSignupException(runnerId, eventId);
        }

        return signupRepository.save(new SignupEntity(runner, event, LocalDateTime.now())).asSignup();
    }

    public List<Signup> getForEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException(eventId);
        }
        return signupRepository.findByEventId(eventId).stream()
                .map(SignupEntity::asSignup)
                .toList();
    }

    public List<Signup> getForRunner(String runnerId) {
        if (!runnerRepository.existsById(runnerId)) {
            throw new RunnerNotFoundException(runnerId);
        }
        return signupRepository.findByRunnerId(runnerId).stream()
                .map(SignupEntity::asSignup)
                .toList();
    }
}
