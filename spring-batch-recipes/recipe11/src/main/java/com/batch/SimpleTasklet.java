package com.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@StepScope // run the main method without this annotation and observe the second execution.
public class SimpleTasklet implements Tasklet {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTasklet.class);
    int count = 0;
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        StepContext stepContext = chunkContext.getStepContext();
        Thread.sleep(2_000);
        LOGGER.info("Executing step : {} for job : {} with" +
                " stepExecutionId: {}, "
                        +" jobExecutionId : {} & JobParameter : {}. Count : {}",
                stepContext.getStepName(),
                stepContext.getJobName(),
                stepContext.getStepExecution().getId(),
                stepContext.getStepExecution().getJobExecution().getId(),
                stepContext.getJobParameters(),
                count);
        stepContext.getStepExecution().getExecutionContext().putInt("count", count);
        if (count == 0) {
            count++;
            return RepeatStatus.CONTINUABLE;
        }
        return RepeatStatus.FINISHED;
    }
}
