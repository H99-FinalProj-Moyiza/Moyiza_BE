package com.example.moyiza_be.batch;

import com.example.moyiza_be.user.entity.User;
import com.example.moyiza_be.user.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest
public class WithdrawUserJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        // Given: Add some users to the database
        user1 = new User();
        user1.setIsDeleted(true);
        user1.setModifiedAtForTesting(LocalDateTime.now().minusMonths(4));
        userRepository.save(user1);

        user2 = new User();
        user2.setIsDeleted(true);
        user2.setModifiedAtForTesting(LocalDateTime.now().minusMonths(2));
        userRepository.save(user2);
    }

    @Test
    public void testWithdrawUserJob() throws Exception {
        // When: Run the job
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // Then: Check the results
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

        // Check that user1 was deleted and user2 was not
        assertFalse(userRepository.findById(user1.getId()).isPresent());
        assertTrue(userRepository.findById(user2.getId()).isPresent());
    }
}