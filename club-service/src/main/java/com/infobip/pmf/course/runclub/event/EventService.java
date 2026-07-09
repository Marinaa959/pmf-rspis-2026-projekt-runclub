package com.infobip.pmf.course.runclub.event;

import com.infobip.pmf.course.runclub.club.exception.ClubNotFoundException;
import com.infobip.pmf.course.runclub.club.storage.ClubRepository;
import com.infobip.pmf.course.runclub.event.exception.EventNotFoundException;
import com.infobip.pmf.course.runclub.event.storage.EventEntity;
import com.infobip.pmf.course.runclub.event.storage.EventRepository;
import com.infobip.pmf.course.runclub.route.RouteClient;
import org.springframework.stereotype.Service;

import java.util.List;

// Pri kreiranju eventa zovi route service da izračuna duljinu i uspon pa rezultat trajno spremi u bazu

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ClubRepository clubRepository;
    private final RouteClient routeClient;

    public EventService(EventRepository eventRepository, ClubRepository clubRepository, RouteClient routeClient) {
        this.eventRepository = eventRepository;
        this.clubRepository = clubRepository;
        this.routeClient = routeClient;
    }

    public List<Event> getAll() {
        return eventRepository.findAll().stream()
                .map(EventEntity::asEvent)
                .toList();
    }

    public Event getOne(Long id) {
        return eventRepository.findById(id)
                .map(EventEntity::asEvent)
                .orElseThrow(() -> new EventNotFoundException(id));
    }

    public List<Event> getForClub(Long clubId) {
        if (!clubRepository.existsById(clubId)) {
            throw new ClubNotFoundException(clubId);
        }
        return eventRepository.findByClubId(clubId).stream()
                .map(EventEntity::asEvent)
                .toList();
    }

    public Event create(Long clubId, CreateEventRequest request) {
        var club = clubRepository.findById(clubId)
                .orElseThrow(() -> new ClubNotFoundException(clubId));

        var routeInfo = routeClient.calculate(request.route());

        var entity = new EventEntity(
                club,
                request.name(),
                request.startTime(),
                routeInfo.distanceMeters(),
                routeInfo.elevationGainMeters()
        );
        return eventRepository.save(entity).asEvent();
    }
}
