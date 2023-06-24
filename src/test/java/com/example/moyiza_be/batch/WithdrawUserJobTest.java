//package com.example.moyiza_be.batch;
//
//import com.example.moyiza_be.user.entity.User;
//import com.example.moyiza_be.user.repository.UserRepository;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.runner.RunWith;
//import org.springframework.batch.core.BatchStatus;
//import org.springframework.batch.core.JobExecution;
//import org.springframework.batch.test.JobLauncherTestUtils;
//import org.springframework.batch.test.context.SpringBatchTest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@RunWith(SpringRunner.class)
//@SpringBatchTest
//@ContextConfiguration(classes = {WithdrawUserJobConfig.class})
//public class WithdrawUserJobTest {
//
//    @Autowired
//    private JobLauncherTestUtils jobLauncherTestUtils;
//
//    @MockBean
//    private UserRepository userRepository;
//
//    private User user1;
//    private User user2;
//
//    @BeforeEach
//    public void setUp() {
//        // Given
//        user1 = new User();
//        user1.setIsDeleted(true);
//        user1.setModifiedAtForTesting(LocalDateTime.now().minusMonths(4));
//        userRepository.save(user1);
//
//        user2 = new User();
//        user2.setIsDeleted(true);
//        user2.setModifiedAtForTesting(LocalDateTime.now().minusMonths(2));
//        userRepository.save(user2);
//    }
//
//    @Test
//    public void testWithdrawUserJob() throws Exception {
//        // When
//        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
//
//        // Then
//        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
//        assertFalse(userRepository.findById(user1.getId()).isPresent());
//        assertTrue(userRepository.findById(user2.getId()).isPresent());
//    }
//}