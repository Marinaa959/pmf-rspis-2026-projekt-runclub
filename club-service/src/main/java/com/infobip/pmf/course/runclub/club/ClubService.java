package com.infobip.pmf.course.runclub.club;

import com.infobip.pmf.course.runclub.club.exception.ClubNotFoundException;
import com.infobip.pmf.course.runclub.club.exception.DuplicateClubException;
import com.infobip.pmf.course.runclub.club.storage.ClubEntity;
import com.infobip.pmf.course.runclub.club.storage.ClubRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubService {

    private final ClubRepository repository;

    public ClubService(ClubRepository repository) {
        this.repository = repository;
    }

    public List<Club> getAll() {
        return repository.findAll().stream()
                .map(ClubEntity::asClub)
                .toList();
    }

    public Club getOne(Long id) {
        return repository.findById(id)
                .map(ClubEntity::asClub)
                .orElseThrow(() -> new ClubNotFoundException(id));
    }

    public Club create(Club club) {
        if (repository.existsByName(club.name())) {
            throw new DuplicateClubException(club.name());
        }
        // id dodjeljuje baza
        var toSave = ClubEntity.from(new Club(null, club.name(), club.city(), club.level(), club.description()));
        return repository.save(toSave).asClub();
    }

    public Club update(Long id, Club club) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ClubNotFoundException(id));

        // promjena imena u već zauzeto -> konflikt
        if (!entity.getName().equals(club.name()) && repository.existsByName(club.name())) {
            throw new DuplicateClubException(club.name());
        }

        entity.setName(club.name());
        entity.setCity(club.city());
        entity.setLevel(club.level());
        entity.setDescription(club.description());
        return repository.save(entity).asClub();
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ClubNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
