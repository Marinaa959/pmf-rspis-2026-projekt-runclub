package com.infobip.pmf.course.runclub.signup;

import com.infobip.pmf.course.runclub.runner.Runner;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
public class SignupController {

    private final SignupService signupService;

    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    @PostMapping("/events/{eventId}/signups")
    @ResponseStatus(HttpStatus.CREATED)
    public Signup signUp(@PathVariable Long eventId, @Valid @NotNull @RequestBody CreateSignupRequest request) {
        return signupService.signUp(eventId, request.runnerId());
    }

    @GetMapping("/events/{eventId}/signups")
    public Signups getForEvent(@PathVariable Long eventId) {
        return new Signups(signupService.getForEvent(eventId));
    }

    @GetMapping("/runners/{runnerId}/signups")
    public Signups getForRunner(@PathVariable @Pattern(regexp = Runner.ID_REGEXP) String runnerId) {
        return new Signups(signupService.getForRunner(runnerId));
    }
}
