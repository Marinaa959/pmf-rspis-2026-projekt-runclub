package com.infobip.pmf.course.runclub.recommendation;

import com.infobip.pmf.course.runclub.runner.Runner;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/runners/{runnerId}/recommendations")
    public String recommend(@PathVariable @Pattern(regexp = Runner.ID_REGEXP) String runnerId) { //identifikacija id-jem
        return recommendationService.recommendClubs(runnerId);
    }
}
