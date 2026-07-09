package com.infobip.pmf.course.runclub;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// cijeli Spring kontekst se uspjesno podiže
@SpringBootTest
@ActiveProfiles("test")
class ClubServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
