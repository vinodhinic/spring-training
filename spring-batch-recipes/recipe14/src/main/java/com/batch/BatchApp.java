package com.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BatchApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchApp.class);

    public static void main(String[] args) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException, JobParametersNotFoundException, NoSuchJobException, JobInstanceAlreadyExistsException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.refresh();

        JobOperator jobOperator = context.getBean(JobOperator.class);

        Long executionId;
        /*executionId = jobOperator.start("avenger-job", "isVillain=true");
        LOGGER.info("Triggered avenger-job {}", executionId);*/

        /*executionId = jobOperator.start("avenger-job", "isVillain=false");
        LOGGER.info("Triggered avenger-job {}", executionId);*/


/*        executionId = jobOperator.start("guardian-job", "isVillain=false");
        LOGGER.info("Triggered guardian-job {}", executionId);*/

        executionId = jobOperator.start("guardian-job", "isVillain=true");
        LOGGER.info("Triggered guardian-job {}", executionId);
    }
}
