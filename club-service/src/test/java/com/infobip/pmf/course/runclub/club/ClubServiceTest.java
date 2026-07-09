package com.infobip.pmf.course.runclub.club;

import com.infobip.pmf.course.runclub.club.exception.ClubNotFoundException;
import com.infobip.pmf.course.runclub.club.exception.DuplicateClubException;
import com.infobip.pmf.course.runclub.club.storage.ClubEntity;
import com.infobip.pmf.course.runclub.club.storage.ClubRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;

//Unit test servisa: repozitorij je mockiran, servis testiran izolirano, bez Spring konteksta i baze

@ExtendWith(MockitoExtension.class)
class ClubServiceTest {

    @Mock
    private ClubRepository repository;

    @InjectMocks
    private ClubService service;

    private ClubEntity entity(Long id, String name) {
        return ClubEntity.from(new Club(id, name, "Zagreb", ClubLevel.INTERMEDIATE, "desc"));
    }

    @Test
    void shouldGetAllClubs() {
        // given
        given(repository.findAll()).willReturn(List.of(entity(1L, "A"), entity(2L, "B")));

        // when
        var clubs = service.getAll();

        // then
        then(clubs).hasSize(2);
        BDDMockito.then(repository).should().findAll();
    }

    @Test
    void shouldGetOneClub() {
        given(repository.findById(1L)).willReturn(Optional.of(entity(1L, "A")));

        var club = service.getOne(1L);

        then(club.name()).isEqualTo("A");
    }

    @Test
    void shouldThrowWhenClubNotFound() {
        given(repository.findById(1L)).willReturn(Optional.empty());

        thenExceptionOfType(ClubNotFoundException.class)
                .isThrownBy(() -> service.getOne(1L))
                .withMessage("Club with id 1 does not exist.");
    }

    @Test
    void shouldCreateClub() {
        var club = new Club(null, "A", "Zagreb", ClubLevel.BEGINNER, "desc");
        given(repository.existsByName("A")).willReturn(false);
        given(repository.save(any(ClubEntity.class))).willAnswer(invocation -> {
            ClubEntity e = invocation.getArgument(0);
            e.setId(1L);
            return e;
        });

        var created = service.create(club);

        then(created.id()).isEqualTo(1L);
        then(created.name()).isEqualTo("A");
    }

    @Test
    void shouldThrowWhenCreatingDuplicateClub() {
        var club = new Club(null, "A", "Zagreb", ClubLevel.BEGINNER, "desc");
        given(repository.existsByName("A")).willReturn(true);

        thenExceptionOfType(DuplicateClubException.class)
                .isThrownBy(() -> service.create(club))
                .withMessage("Club with name 'A' already exists.");

        BDDMockito.then(repository).should(never()).save(any());
    }

    @Test
    void shouldDeleteExistingClub() {
        given(repository.existsById(1L)).willReturn(true);

        service.delete(1L);

        BDDMockito.then(repository).should().deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonexistentClub() {
        given(repository.existsById(1L)).willReturn(false);

        thenExceptionOfType(ClubNotFoundException.class)
                .isThrownBy(() -> service.delete(1L));

        BDDMockito.then(repository).should(never()).deleteById(any());
    }
}
