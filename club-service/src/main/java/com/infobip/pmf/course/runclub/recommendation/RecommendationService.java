package com.infobip.pmf.course.runclub.recommendation;

import com.infobip.pmf.course.runclub.club.storage.ClubEntity;
import com.infobip.pmf.course.runclub.club.storage.ClubRepository;
import com.infobip.pmf.course.runclub.runner.exception.RunnerNotFoundException;
import com.infobip.pmf.course.runclub.runner.storage.RunnerRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

// Podaci (klubovi) dolaze iz relacijske baze obicnim upitom, pa vektorska pretraga nije potrebna

@Service
public class RecommendationService {

    private static final String SYSTEM_PROMPT = """
            Ti si asistent run club platforme koji trkacima preporučuje klubove za trčanje.
            Na temelju profila trkača i popisa dostupnih klubova, preporuči najviše 3 kluba
            koji mu najbolje odgovaraju s obzirom na razinu i grad i za svaki u jednoj rečenici objasni zašto.
            Odgovaraj na hrvatskom jeziku, sažeto i u prijateljskom tonu.
            Ako nijedan klub ne odgovara, iskreno to reci.
            """;

    private static final String USER_PROMPT = """
            Profil trkača: %s
            Dostupni klubovi:
            %s
            Preporuči klubove za ovog trkača.""";

    private static final String FALLBACK_RESPONSE = "Trenutno ne mogu generirati preporuku. Pokušajte ponovno.";

    private final ChatClient chatClient;
    private final RunnerRepository runnerRepository;
    private final ClubRepository clubRepository;

    public RecommendationService(ChatClient.Builder chatClientBuilder,
                                 RunnerRepository runnerRepository,
                                 ClubRepository clubRepository) {
        // Spring AI auto-konfigurira ChatClient.Builder - samo ga injektiramo
        this.chatClient = chatClientBuilder
                .defaultSystem(SYSTEM_PROMPT)
                .build();
        this.runnerRepository = runnerRepository;
        this.clubRepository = clubRepository;
    }

    public String recommendClubs(String runnerId) {
        var runner = runnerRepository.findById(runnerId)
                .orElseThrow(() -> new RunnerNotFoundException(runnerId));

        var runnerProfile = "%s %s, preferirana razina: %s".formatted(
                runner.getFirstName(), runner.getLastName(), runner.getPreferredLevel());

        var clubCandidates = clubRepository.findAll().stream()
                .map(this::describe)
                .collect(Collectors.joining("\n"));

        if (clubCandidates.isBlank()) {
            return "Trenutno nema registriranih klubova za preporuku.";
        }

        var content = chatClient
                .prompt(USER_PROMPT.formatted(runnerProfile, clubCandidates))
                .call()
                .content();

        return content != null ? content : FALLBACK_RESPONSE;
    }

    private String describe(ClubEntity club) {
        return "- %s (%s, razina %s): %s".formatted(
                club.getName(), club.getCity(), club.getLevel(),
                club.getDescription() != null ? club.getDescription() : "bez opisa");
    }
}
