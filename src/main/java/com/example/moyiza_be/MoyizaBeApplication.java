package com.example.moyiza_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MoyizaBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoyizaBeApplication.class, args);
    }

}
