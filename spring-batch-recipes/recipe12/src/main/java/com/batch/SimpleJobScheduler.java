package com.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SimpleJobScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleJobScheduler.class);

    @Autowired
    @Qualifier("simpleJob")
    private Job job;

    @Autowired
    private JobOperator jobOperator;

    @Scheduled(fixedDelay = 5_000L)
    public void runSimpleJob() {
        try {
            Long executionId = jobOperator.startNextInstance(job.getName());
            LOGGER.info("Triggered {} . Execution ID : {}", job.getName(), executionId);
        } catch (NoSuchJobException | JobParametersNotFoundException | JobRestartException | JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    // Uncomment the below and understand the difference between fixedDelay and fixedRate.
    /*
    AtomicInteger count = new AtomicInteger(0);
    @Scheduled(fixedDelay = 2_000L)
    public void run() throws InterruptedException {
        int c = count.getAndIncrement();
        System.out.println("Starting "+c);
        if(c < 2) {
            Thread.sleep(10_000L);
        }
        System.out.println("Ending : "+c);
    }
     */
}
