package com.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

public class BatchApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchApp.class);

    public static void main(String[] args) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.refresh();
        JobLauncher jobLauncher = context.getBean("jobLauncher", JobLauncher.class);
        Job job = context.getBean("simpleJob", Job.class);

        JobParameter jobParameter = new JobParameter(System.currentTimeMillis());
        Map<String, JobParameter> paramMap = Map.of("currentTime", jobParameter);
        JobExecution jobExecution = jobLauncher.run(job, new JobParameters(paramMap));
        LOGGER.info("Triggered JobExecution {} with parameter {}", jobExecution.getId(), paramMap);

        jobParameter = new JobParameter(System.currentTimeMillis());
        paramMap = Map.of("currentTime", jobParameter);
        jobExecution = jobLauncher.run(job, new JobParameters(paramMap));
        LOGGER.info("Triggered JobExecution {} with parameter {}", jobExecution.getId(), paramMap);
    }
}
