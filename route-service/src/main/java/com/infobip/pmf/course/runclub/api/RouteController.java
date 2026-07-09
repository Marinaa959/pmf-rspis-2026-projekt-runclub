package com.infobip.pmf.course.runclub.api;

import com.infobip.pmf.course.runclub.route.RouteInfo;
import com.infobip.pmf.course.runclub.route.RouteRequest;
import com.infobip.pmf.course.runclub.route.RouteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Validated
@RestController
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping
    public RouteInfo calculate(@Valid @NotNull @RequestBody RouteRequest request) {
        return routeService.calculate(request);
    }
}
