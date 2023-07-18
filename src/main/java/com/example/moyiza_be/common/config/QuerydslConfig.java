package com.example.moyiza_be.common.config;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuerydslConfig {

    @Autowired
    EntityManager entityManager;
    @Bean
    public JPAQueryFactory jpaQueryFactory()
    {
        return new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
    }

}
