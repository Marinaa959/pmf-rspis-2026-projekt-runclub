package com.infobip.pmf.course.runclub.route;

import com.infobip.pmf.course.runclub.config.RedisConfig;
import com.infobip.pmf.course.runclub.route.ors.OrsClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

// izračun rute s Redis cacheom

@Service
public class RouteService {

    private final OrsClient orsClient;

    public RouteService(OrsClient orsClient) {
        this.orsClient = orsClient;
    }

    @Cacheable(value = RedisConfig.ROUTES_CACHE, key = "#request.cacheKey()") // prvi poziv s danim koordinatama ide na OpenRouteService i sprema rezultat u Redis
    // ključ je kanonski string koordinata jer Redis ključevi moraju biti stringovi
    public RouteInfo calculate(RouteRequest request) {
        return orsClient.calculate(request);
    }
}
