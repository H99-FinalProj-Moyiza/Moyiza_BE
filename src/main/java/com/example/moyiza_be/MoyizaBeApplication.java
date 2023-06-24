package com.example.moyiza_be;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableEncryptableProperties
@EnableCaching
@EnableBatchProcessing
@EnableScheduling
public class MoyizaBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoyizaBeApplication.class, args);
    }

}
