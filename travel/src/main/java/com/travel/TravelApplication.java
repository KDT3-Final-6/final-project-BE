package com.travel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableJpaAuditing
@SpringBootApplication
public class TravelApplication {

    @PostConstruct
    public void timeZoneSetting() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    public static void main(String[] args) {
        SpringApplication.run(TravelApplication.class, args);
    }

}
