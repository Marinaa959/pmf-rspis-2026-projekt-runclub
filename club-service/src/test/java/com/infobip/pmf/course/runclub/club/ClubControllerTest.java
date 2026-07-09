package com.infobip.pmf.course.runclub.club;

import com.infobip.pmf.course.runclub.club.storage.ClubRepository;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.BDDAssertions.then;

//Integracijski test: diže cijelu aplikaciju na random portu,
// šalje prave HTTP zahtjeve RestClientom i provjerava JSON odgovore JSONAssertom.

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ClubControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ClubRepository repository;

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .defaultStatusHandler(status -> !status.is2xxSuccessful(), (req, res) -> {
                })
                .build();
    }

    private long createClub(String name, String city, String level) {
        var body = """
                { "name": "%s", "city": "%s", "level": "%s" }""".formatted(name, city, level);
        var response = restClient.post()
                .uri("/clubs")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toEntity(String.class);
        then(response.getStatusCode().value()).isEqualTo(201);
        return Long.parseLong(response.getBody().replaceAll(".*\"id\"\\s*:\\s*(\\d+).*", "$1"));
    }

    @Test
    void shouldCreateClub() throws JSONException {
        var response = restClient.post()
                .uri("/clubs")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        { "name": "Zagreb Runners", "city": "Zagreb", "level": "INTERMEDIATE" }""")
                .retrieve()
                .toEntity(String.class);

        then(response.getStatusCode().value()).isEqualTo(201);
        JSONAssert.assertEquals("""
                { "name": "Zagreb Runners", "city": "Zagreb", "level": "INTERMEDIATE" }""",
                response.getBody(), false); // false = ne provjerava id (auto-generiran)
    }

    @Test
    void shouldGetClubById() throws JSONException {
        var id = createClub("Sljeme Trail", "Zagreb", "ADVANCED");

        var response = restClient.get().uri("/clubs/" + id).retrieve().toEntity(String.class);

        then(response.getStatusCode().value()).isEqualTo(200);
        JSONAssert.assertEquals("""
                { "id": %d, "name": "Sljeme Trail", "city": "Zagreb", "level": "ADVANCED" }"""
                .formatted(id), response.getBody(), false);
    }

    @Test
    void shouldReturnNotFoundForMissingClub() throws JSONException {
        var response = restClient.get().uri("/clubs/9999").retrieve().toEntity(String.class);

        then(response.getStatusCode().value()).isEqualTo(404);
        JSONAssert.assertEquals("""
                { "message": "Club with id 9999 does not exist." }""",
                response.getBody(), true);
    }

    @Test
    void shouldReturnConflictForDuplicateName() throws JSONException {
        createClub("Zagreb Runners", "Zagreb", "INTERMEDIATE");

        var response = restClient.post()
                .uri("/clubs")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        { "name": "Zagreb Runners", "city": "Split", "level": "BEGINNER" }""")
                .retrieve()
                .toEntity(String.class);

        then(response.getStatusCode().value()).isEqualTo(409);
        JSONAssert.assertEquals("""
                { "message": "Club with name 'Zagreb Runners' already exists." }""",
                response.getBody(), true);
    }

    @Test
    void shouldReturnBadRequestWhenNameMissing() {
        var response = restClient.post()
                .uri("/clubs")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        { "city": "Zagreb", "level": "BEGINNER" }""")
                .retrieve()
                .toEntity(String.class);

        then(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void shouldDeleteClub() {
        var id = createClub("To Delete", "Zagreb", "BEGINNER");

        var deleteResponse = restClient.delete().uri("/clubs/" + id).retrieve().toEntity(String.class);
        then(deleteResponse.getStatusCode().value()).isEqualTo(204);

        var getResponse = restClient.get().uri("/clubs/" + id).retrieve().toEntity(String.class);
        then(getResponse.getStatusCode().value()).isEqualTo(404);
    }
}
