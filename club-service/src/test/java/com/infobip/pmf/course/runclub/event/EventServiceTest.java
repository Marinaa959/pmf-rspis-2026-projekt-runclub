package com.infobip.pmf.course.runclub.event;

import com.infobip.pmf.course.runclub.club.Club;
import com.infobip.pmf.course.runclub.club.ClubLevel;
import com.infobip.pmf.course.runclub.club.exception.ClubNotFoundException;
import com.infobip.pmf.course.runclub.club.storage.ClubEntity;
import com.infobip.pmf.course.runclub.club.storage.ClubRepository;
import com.infobip.pmf.course.runclub.event.storage.EventEntity;
import com.infobip.pmf.course.runclub.event.storage.EventRepository;
import com.infobip.pmf.course.runclub.route.RouteClient;
import com.infobip.pmf.course.runclub.route.RouteInfo;
import com.infobip.pmf.course.runclub.route.RoutePoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;

//Unit test EventServicea: RouteClient (poziv prema drugom mikroservisu) je MOCKIRAN,
// pa test ne ovisi o route-serviceu ni o mrezi

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private RouteClient routeClient;

    @InjectMocks
    private EventService service;

    private final List<RoutePoint> route = List.of(
            new RoutePoint(45.8003, 15.9711),
            new RoutePoint(45.7769, 15.9788)
    );

    private final CreateEventRequest request =
            new CreateEventRequest("Jarun 10k", LocalDateTime.of(2026, 10, 11, 9, 0), route);

    @Test
    void shouldCreateEventWithCalculatedRoute() {
        // given
        var club = ClubEntity.from(new Club(1L, "Zagreb Runners", "Zagreb", ClubLevel.INTERMEDIATE, null));
        given(clubRepository.findById(1L)).willReturn(Optional.of(club));
        given(routeClient.calculate(route)).willReturn(new RouteInfo(10240.5, 87.2));
        given(eventRepository.save(any(EventEntity.class))).willAnswer(inv -> inv.getArgument(0));

        // when
        var event = service.create(1L, request);

        // then: duljina i uspon dolaze iz route-servicea i spremaju se u event
        then(event.distanceMeters()).isEqualTo(10240.5);
        then(event.elevationGainMeters()).isEqualTo(87.2);
        then(event.name()).isEqualTo("Jarun 10k");
        BDDMockito.then(routeClient).should().calculate(route);
        BDDMockito.then(eventRepository).should().save(any(EventEntity.class));
    }

    @Test
    void shouldNotCallRouteServiceWhenClubDoesNotExist() {
        // given
        given(clubRepository.findById(1L)).willReturn(Optional.empty());

        // when, then
        thenExceptionOfType(ClubNotFoundException.class)
                .isThrownBy(() -> service.create(1L, request));

        BDDMockito.then(routeClient).should(never()).calculate(any());
        BDDMockito.then(eventRepository).should(never()).save(any());
    }
}
