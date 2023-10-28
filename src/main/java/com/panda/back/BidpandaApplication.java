package com.panda.back;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@OpenAPIDefinition(servers = {@Server(url = "https://bidpanda-server.dewey-89.com",description = "Default Server URL")})
@EnableJpaAuditing
@SpringBootApplication
public class BidpandaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BidpandaApplication.class, args);

    }

}
