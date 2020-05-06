package com.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class JobConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobConfig.class);
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Step simpleOddStep() {
        return stepBuilderFactory.get("simple-odd-step")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        StepContext stepContext = chunkContext.getStepContext();
                        LOGGER.info("Executing step : {} for job : {} with" +
                                        " stepExecutionId: {}, "
                                        + " jobExecutionId : {} & JobParameter : {}. Even/Odd : {}",
                                stepContext.getStepName(),
                                stepContext.getJobName(),
                                stepContext.getStepExecution().getId(),
                                stepContext.getStepExecution().getJobExecution().getId(),
                                stepContext.getJobParameters(),
                                "odd");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Bean
    public Step simpleEvenStep() {
        // note the lambda here. compare with the tasklet defined at simpleOddStep()
        return stepBuilderFactory.get("simple-even-step")
                .tasklet((contribution, chunkContext) -> {
                    StepContext stepContext = chunkContext.getStepContext();
                    LOGGER.info("Executing step : {} for job : {} with" +
                                    " stepExecutionId: {}, "
                                    + " jobExecutionId : {} & JobParameter : {}. Even/Odd : {}",
                            stepContext.getStepName(),
                            stepContext.getJobName(),
                            stepContext.getStepExecution().getId(),
                            stepContext.getStepExecution().getJobExecution().getId(),
                            stepContext.getJobParameters(),
                            "even");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public JobExecutionDecider decider() {
        // understand this lambda. It is the same as below :
        /*return new JobExecutionDecider() {
            @Override
            public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
                return null;
            }
        };*/
        return (jobExecution, stepExecution) -> {
            Long runId = jobExecution.getJobParameters().getLong("run.id");
            if (runId % 2 == 0) {
                return new FlowExecutionStatus("EVEN");
            }
            return new FlowExecutionStatus("ODD");
        };
    }

    @Bean(name = "simpleJob")
    public Job simpleJob() {
        Flow oddEvenFlow =
                new FlowBuilder<Flow>("simple-flow")
                        .start(decider())
                        .on("EVEN").to(simpleEvenStep())
                        .from(decider()).on("ODD").to(simpleOddStep())
                        .build();

        return jobBuilderFactory.get("simple-job")
                .incrementer(new RunIdIncrementer())
                .start(oddEvenFlow).end().build();
    }

    @Bean
    public Step flakyStep() {
        return stepBuilderFactory.get("flaky-step")
                .tasklet((contribution, chunkContext) -> {
                    Random random = new Random();
                    StepContext stepContext = chunkContext.getStepContext();

                    if(random.nextInt(1000) %2 == 0) {
                        LOGGER.info("Throwing intentional exception while Executing step : {} for job : {} with" +
                                        " stepExecutionId: {}, "
                                        + " jobExecutionId : {} & JobParameter : {}",
                                stepContext.getStepName(),
                                stepContext.getJobName(),
                                stepContext.getStepExecution().getId(),
                                stepContext.getStepExecution().getJobExecution().getId(),
                                stepContext.getJobParameters()
                                );
                        throw new RuntimeException("Intentional exception");
                    }
                    LOGGER.info("Executing step : {} for job : {} with" +
                                    " stepExecutionId: {}, "
                                    + " jobExecutionId : {} & JobParameter : {}",
                            stepContext.getStepName(),
                            stepContext.getJobName(),
                            stepContext.getStepExecution().getId(),
                            stepContext.getStepExecution().getJobExecution().getId(),
                            stepContext.getJobParameters()
                            );
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step backupStep() {
        return stepBuilderFactory.get("backup-step")
                .tasklet((contribution, chunkContext) -> {
                    StepContext stepContext = chunkContext.getStepContext();

                    LOGGER.info("Executing backup step : {} for job : {} with" +
                                    " stepExecutionId: {}, "
                                    + " jobExecutionId : {} & JobParameter : {}",
                            stepContext.getStepName(),
                            stepContext.getJobName(),
                            stepContext.getStepExecution().getId(),
                            stepContext.getStepExecution().getJobExecution().getId(),
                            stepContext.getJobParameters()
                    );
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean(name = "flakyJob")
    public Job flakyJob() {
        return jobBuilderFactory.get("flaky-job")
                .incrementer(new RunIdIncrementer())
                .start(flakyStep())
                .on("FAILED").to(backupStep())
                .from(flakyStep()).on("COMPLETED").end()
                .from(backupStep()).end().build();
    }
}
