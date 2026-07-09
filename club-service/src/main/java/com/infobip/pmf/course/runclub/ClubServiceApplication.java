package com.infobip.pmf.course.runclub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan // ucitava @ConfigurationProperties record-e (AppProperties)
public class ClubServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClubServiceApplication.class, args);
    }
}
