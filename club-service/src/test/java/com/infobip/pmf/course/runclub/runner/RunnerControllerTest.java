package com.infobip.pmf.course.runclub.runner;

import com.infobip.pmf.course.runclub.runner.storage.RunnerRepository;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RunnerControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RunnerRepository repository;

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

    private void createRunner(String id) {
        var response = restClient.post()
                .uri("/runners")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        { "id": "%s", "firstName": "Marina", "lastName": "K",
                          "email": "m@example.com", "preferredLevel": "INTERMEDIATE" }""".formatted(id))
                .retrieve()
                .toEntity(String.class);
        then(response.getStatusCode().value()).isEqualTo(201);
    }

    @Test
    void shouldCreateAndGetRunner() throws JSONException {
        createRunner("marina-k");

        var response = restClient.get().uri("/runners/marina-k").retrieve().toEntity(String.class);

        then(response.getStatusCode().value()).isEqualTo(200);
        JSONAssert.assertEquals("""
                { "id": "marina-k", "firstName": "Marina", "lastName": "K",
                  "email": "m@example.com", "preferredLevel": "INTERMEDIATE" }""",
                response.getBody(), true);
    }

    @Test
    void shouldRejectInvalidRunnerId() {
        // veliko slovo nije dopusteno patternom -> 400 na validaciji parametra putanje
        var response = restClient.get().uri("/runners/INVALID-ID").retrieve().toEntity(String.class);

        then(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void shouldReturnConflictForDuplicateRunner() throws JSONException {
        createRunner("marina-k");

        var response = restClient.post()
                .uri("/runners")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        { "id": "marina-k", "firstName": "Jonny", "lastName": "Glendinning",
                          "preferredLevel": "BEGINNER" }""")
                .retrieve()
                .toEntity(String.class);

        then(response.getStatusCode().value()).isEqualTo(409);
        JSONAssert.assertEquals("""
                { "message": "Runner with id 'marina-k' already exists." }""",
                response.getBody(), true);
    }

    @Test
    void shouldReturnConflictWhenChangingIdOnUpdate() throws JSONException {
        createRunner("marina-k");

        var response = restClient.put()
                .uri("/runners/marina-k")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        { "id": "new-id", "firstName": "Marina", "lastName": "K",
                          "preferredLevel": "INTERMEDIATE" }""")
                .retrieve()
                .toEntity(String.class);

        then(response.getStatusCode().value()).isEqualTo(409);
        JSONAssert.assertEquals("""
                { "message": "Updating runner's id is not allowed." }""",
                response.getBody(), true);
    }

    @Test
    void shouldReturnNotFoundForMissingRunner() throws JSONException {
        var response = restClient.get().uri("/runners/ghost").retrieve().toEntity(String.class);

        then(response.getStatusCode().value()).isEqualTo(404);
        JSONAssert.assertEquals("""
                { "message": "Runner with id 'ghost' does not exist." }""",
                response.getBody(), true);
    }
}
