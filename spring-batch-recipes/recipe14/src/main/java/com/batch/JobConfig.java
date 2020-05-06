package com.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class JobConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobConfig.class);

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private ComicCharacterReader comicCharacterReader;

    @Bean
    public ItemProcessor<ComicCharacter, String> avengerComicCharacterProcessor() {
        return new ComicCharacterProcessor("Avenger");
    }

    @Bean
    public ItemProcessor<ComicCharacter, String> guardiansComicCharacterProcessor() {
        return new ComicCharacterProcessor("Guardians");
    }

    @Bean
    public ItemWriter<String> consoleWriter() {
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> items) throws Exception {
                System.out.println("Received " + items.size() + " items :" + items);
            }
        };
    }

    @Bean
    public StepExecutionListener stepExecutionListener() {
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                LOGGER.info("Before executing step {} for executionId {}", stepExecution.getStepName(),
                        stepExecution.getJobExecutionId());
            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                LOGGER.info("After executing step {} for executionId {}. Status :" +
                                "Read {}, filtered {}, written {}", stepExecution.getStepName(),
                        stepExecution.getJobExecutionId(),
                        stepExecution.getReadCount(),
                        stepExecution.getFilterCount(),
                        stepExecution.getWriteCount());
                return stepExecution.getExitStatus();
            }
        };
    }

    @Bean
    public Step avengerStep() {
        return stepBuilderFactory.get("avenger-step")
                .<ComicCharacter, String>chunk(3)
                .reader(comicCharacterReader)
                .processor(avengerComicCharacterProcessor())
                .writer(consoleWriter())
                .listener(stepExecutionListener())
                .build();
    }

    @Bean
    public Step guardianStep() {
        return stepBuilderFactory.get("guardian-step")
                .<ComicCharacter, String>chunk(3)
                .reader(comicCharacterReader)
                .processor(guardiansComicCharacterProcessor())
                .writer(consoleWriter())
                .listener(new ChunkListener() {
                    @Override
                    public void beforeChunk(ChunkContext context) {
                        LOGGER.info("Executing chunk for step {}", context.getStepContext().getStepName());
                    }

                    @Override
                    public void afterChunk(ChunkContext context) {
                        LOGGER.info("After chunk for step {}", context.getStepContext().getStepName());
                    }

                    @Override
                    public void afterChunkError(ChunkContext context) {
                    }
                })
                .listener(stepExecutionListener())
                .build();
    }


    @Bean(name="avengerJob")
    public Job avengerJob() {
        return jobBuilderFactory.get("avenger-job")
                .start(avengerStep())
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        LOGGER.info("Before executing {} for job {}", jobExecution.getId(), jobExecution.getJobInstance().getJobName());
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        LOGGER.info("After executing {} for job {}", jobExecution.getId(), jobExecution.getJobInstance().getJobName());
                    }
                })
                .build();
    }

    @Bean
    public Job guardianJob() {
        return jobBuilderFactory.get("guardian-job")
                .start(guardianStep())
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        LOGGER.info("Before executing {} for job {}", jobExecution.getId(), jobExecution.getJobInstance().getJobName());
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        LOGGER.info("After executing {} for job {}", jobExecution.getId(), jobExecution.getJobInstance().getJobName());
                    }
                }).build();
    }
}
