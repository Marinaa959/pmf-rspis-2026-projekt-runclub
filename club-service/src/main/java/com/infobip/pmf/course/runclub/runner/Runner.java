package com.infobip.pmf.course.runclub.runner;

import com.infobip.pmf.course.runclub.club.ClubLevel;
import jakarta.validation.constraints.*;

public record Runner(
        @NotNull @Pattern(regexp = Runner.ID_REGEXP) String id,  // string koji korisnik sam izabire
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email String email,
        @NotNull ClubLevel preferredLevel
) {
    public static final String ID_REGEXP = "^[a-z0-9_-]{3,30}$";
}
