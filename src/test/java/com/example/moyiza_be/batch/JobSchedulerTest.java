//package com.example.moyiza_be.batch;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobExecution;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.test.MetaDataInstanceFactory;
//
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class JobSchedulerTest {
//
//    @Mock
//    private JobLauncher jobLauncher;
//    @Mock
//    private Job withdrawUserJob;
//    @Mock
//    private Job cleanUpEntitiesJob;
//    @InjectMocks
//    private JobScheduler scheduler;
//
//    @Test
//    @DisplayName("runWithdrawUserJob")
//    public void runWithdrawUserJob() throws Exception {
//        // Given
//        JobExecution jobExecution = MetaDataInstanceFactory.createJobExecution();
//        when(jobLauncher.run(withdrawUserJob, new JobParameters())).thenReturn(jobExecution);
//        // When
//        scheduler.runWithdrawUserJob();
//        // Then
//        verify(jobLauncher, times(1)).run(withdrawUserJob, new JobParameters());
//    }
//
//    @Test
//    @DisplayName("runCleanUpEntitiesJob")
//    public void runCleanUpEntitiesJob() throws Exception {
//        //Given
//        JobExecution jobExecution = MetaDataInstanceFactory.createJobExecution();
//        when(jobLauncher.run(cleanUpEntitiesJob, new JobParameters())).thenReturn(jobExecution);
//        //when
//        scheduler.runCleanUpEntitiesJob();
//        //THen
//        verify(jobLauncher, times(1)).run(cleanUpEntitiesJob, new JobParameters());
//    }
//}