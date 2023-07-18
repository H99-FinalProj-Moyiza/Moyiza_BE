package com.example.moyiza_be.common.common_features.batch;

import com.example.moyiza_be.domain.club.repository.ClubRepository;
import com.example.moyiza_be.domain.event.repository.EventRepository;
import com.example.moyiza_be.domain.oneday.repository.OneDayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class CleanUpEntitiesJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final ClubRepository clubRepository;
    private final OneDayRepository oneDayRepository;
    private final EventRepository eventRepository;

    @Bean
    public Job cleanUpEntitiesJob() {
        return new JobBuilder("cleanUpEntitiesJob", jobRepository)
                .start(cleanUpClubEntityStep())
                .next(cleanUpOneDayEntityStep())
                .next(cleanUpEventEntityStep())
                .build();
    }

    public Step cleanUpClubEntityStep(){
        Tasklet tasklet = ((contribution, chunkContext) -> {
            LocalDateTime targetDate = LocalDateTime.now().minusMonths(1);
            clubRepository.cleanUpDeletedClubs(targetDate);
            return RepeatStatus.FINISHED;
        });
        return new StepBuilder("cleanUpClubEntityStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

    public Step cleanUpOneDayEntityStep(){
        Tasklet tasklet = ((contribution, chunkContext) ->{
            LocalDateTime targetDate = LocalDateTime.now().minusMonths(1);
            oneDayRepository.cleanUpDeletedOneDays(targetDate);
            return RepeatStatus.FINISHED;
        });
        return new StepBuilder("cleanUpOneDayEntityStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

    public Step cleanUpEventEntityStep(){
        Tasklet tasklet = ((contribution, chunkContext) -> {
            LocalDateTime targetDate = LocalDateTime.now().minusMonths(1);
            eventRepository.cleanUpDeletedEvents(targetDate);
            return RepeatStatus.FINISHED;
        });
        return new StepBuilder("cleanUpEventEntityStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

}
