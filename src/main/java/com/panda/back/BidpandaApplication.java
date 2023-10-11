package com.panda.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BidpandaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BidpandaApplication.class, args);

    }

}
