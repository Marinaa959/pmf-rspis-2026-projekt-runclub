package com.infobip.pmf.course.runclub.runner;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/runners")
public class RunnerController {

    private final RunnerService runnerService;

    public RunnerController(RunnerService runnerService) {
        this.runnerService = runnerService;
    }

    @GetMapping
    public Runners getAll() {
        return new Runners(runnerService.getAll());
    }

    @GetMapping("/{id}")
    public Runner getOne(@PathVariable @Pattern(regexp = Runner.ID_REGEXP) String id) {
        return runnerService.getOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Runner create(@Valid @NotNull @RequestBody Runner runner) {
        return runnerService.create(runner);
    }

    @PutMapping("/{id}")
    public Runner update(
            @PathVariable @Pattern(regexp = Runner.ID_REGEXP) String id,
            @Valid @NotNull @RequestBody Runner runner
    ) {
        return runnerService.update(id, runner);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Pattern(regexp = Runner.ID_REGEXP) String id) {
        runnerService.delete(id);
    }
}
