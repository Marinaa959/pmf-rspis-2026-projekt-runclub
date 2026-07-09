package com.infobip.pmf.course.runclub.route;

import com.infobip.pmf.course.runclub.route.exception.RouteCalculationException;
import com.infobip.pmf.course.runclub.route.ors.OrsClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenExceptionOfType;
import static org.mockito.BDDMockito.given;

// Unit test RouteServiceaa: OrsClient je mockiran.
// @Cacheable se ne aktivira u ovom testu jer nema Spring konteksta, cache se provjerava ručno

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {

    @Mock
    private OrsClient orsClient;

    @InjectMocks
    private RouteService service;

    private final RouteRequest request = new RouteRequest(List.of(
            new RoutePoint(45.7818, 15.9182),
            new RoutePoint(45.7752, 15.9224)
    ));

    @Test
    void shouldReturnRouteInfoFromOrs() {
        given(orsClient.calculate(request)).willReturn(new RouteInfo(1234.5, 42.0));

        var info = service.calculate(request);

        then(info.distanceMeters()).isEqualTo(1234.5);
        then(info.elevationGainMeters()).isEqualTo(42.0);
        BDDMockito.then(orsClient).should().calculate(request);
    }

    @Test
    void shouldPropagateCalculationError() {
        given(orsClient.calculate(request))
                .willThrow(new RouteCalculationException("boom", null));

        thenExceptionOfType(RouteCalculationException.class)
                .isThrownBy(() -> service.calculate(request));
    }

    @Test
    void shouldBuildStableCacheKeyFromCoordinates() {
        var key1 = request.cacheKey();
        var key2 = new RouteRequest(List.of(
                new RoutePoint(45.7818, 15.9182),
                new RoutePoint(45.7752, 15.9224)
        )).cacheKey();
        then(key1).isEqualTo(key2);
    }
}
