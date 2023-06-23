package com.example.moyiza_be.batch;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.MetaDataInstanceFactory;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WithdrawUserJobSchedulerTest {

    @Mock
    private JobLauncher jobLauncher;
    @Mock
    private Job withdrawUserJob;
    @InjectMocks
    private WithdrawUserJobScheduler scheduler;

    @Test
    public void runJob() throws Exception {
        // Given
        JobExecution jobExecution = MetaDataInstanceFactory.createJobExecution();
        when(jobLauncher.run(withdrawUserJob, new JobParameters())).thenReturn(jobExecution);
        // When
        scheduler.runJob();
        // Then
        verify(jobLauncher, times(1)).run(withdrawUserJob, new JobParameters());
    }
}