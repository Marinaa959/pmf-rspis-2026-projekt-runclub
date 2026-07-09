package com.infobip.pmf.course.runclub.signup;

import com.infobip.pmf.course.runclub.runner.Runner;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateSignupRequest(
        @NotNull @Pattern(regexp = Runner.ID_REGEXP) String runnerId
) {
}
