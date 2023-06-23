package com.example.moyiza_be.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;
import org.springframework.batch.core.JobParameters;
import org.springframework.scheduling.annotation.Scheduled;

@Component
@RequiredArgsConstructor
public class JobScheduler {

    private final JobLauncher jobLauncher;
    private final Job withdrawUserJob;
    private final Job cleanUpEntitiesJob;

    //매주 일요일 0시에 job 실행
    @Scheduled(cron = "0 0 0 * * SUN")
    public void runWithdrawUserJob() throws Exception {
        jobLauncher.run(withdrawUserJob, new JobParameters());
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void runCleanUpEntitiesJob() throws Exception {
        jobLauncher.run(cleanUpEntitiesJob, new JobParameters());
    }
}
