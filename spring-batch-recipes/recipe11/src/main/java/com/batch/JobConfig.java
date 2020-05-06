package com.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.config.Task;

@Configuration
public class JobConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private Tasklet simpleTasklet;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Step simpleStep() {
        return stepBuilderFactory.get("simple-step")
                .tasklet(simpleTasklet)
                .build();
    }

    @Bean(name="simpleJob")
    public Job simpleJob() {
        return jobBuilderFactory.get("simple-job").start(simpleStep()).build();
    }
}
