package com.example.moyiza_be.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;
import org.springframework.batch.core.JobParameters;
import org.springframework.scheduling.annotation.Scheduled;

@Component
@RequiredArgsConstructor
public class WithdrawUserJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job withdrawUserJob;

    //매주 일요일 0시에 job 실행
    @Scheduled(cron = "0 0 0 * * SUN")
    public void runJob() throws Exception {
        jobLauncher.run(withdrawUserJob, new JobParameters());
    }
}
