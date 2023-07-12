package com.example.moyiza_be.batch;

import com.example.moyiza_be.user.entity.User;
import com.example.moyiza_be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class WithdrawUserJobConfig {

    private final UserRepository userRepository;

    //Job 설정
    @Bean
    public Job withdrawUserJob(JobRepository jobRepository, Step withdrawJobstep){
        return new JobBuilder("withdrawUserJob", jobRepository)
                .start(withdrawJobstep)
                .build();
    }

    //Step 설정
    @Bean
    public Step withdrawJobStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("withdrawUserStep", jobRepository)
                .<User, User>chunk(10, transactionManager)
                .reader(withdrawUserReader())
                .processor(withdrawUserProcessor())
                .writer(withdrawUserWriter())
                .build();
    }

    //Reader 설정
    @Bean
    @StepScope
    public QueueItemReader<User> withdrawUserReader() {
        List<User> targetUsers = userRepository.findByModifiedAtBeforeAndIsDeletedTrue(
                LocalDateTime.now().minusMonths(3));
        return new QueueItemReader<>(targetUsers);
    }

    //Processor 설정
    //따로 가공이 필요하지 않아 reader에서 읽어온 값 그대로 반환
    public ItemProcessor<User, User> withdrawUserProcessor() {
        return user -> user;
    }

    //Writer
    public ItemWriter<User> withdrawUserWriter() {
        return ((Chunk<? extends User> users) -> userRepository.deleteAll(users));
    }
}