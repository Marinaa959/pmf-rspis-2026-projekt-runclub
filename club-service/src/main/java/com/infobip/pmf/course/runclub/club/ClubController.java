package com.infobip.pmf.course.runclub.club;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/clubs")
public class ClubController {

    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping
    public Clubs getAll() {
        return new Clubs(clubService.getAll());
    }

    @GetMapping("/{id}")
    public Club getOne(@PathVariable Long id) {
        return clubService.getOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Club create(@Valid @NotNull @RequestBody Club club) {
        return clubService.create(club);
    }

    @PutMapping("/{id}")
    public Club update(@PathVariable Long id, @Valid @NotNull @RequestBody Club club) {
        return clubService.update(id, club);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        clubService.delete(id);
    }
}
