package com.batch;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.batch.DataUtil.heroCharacters;
import static com.batch.DataUtil.villainCharacters;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestAppConfig.class})
public class BatchTest {

    @Autowired
    @Qualifier("avengerJob")
    private Job avengerJob;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobExplorer jobExplorer;

    @Before
    public void setup() {
        jobLauncherTestUtils.setJob(avengerJob);
    }

    @Test
    public void testAvengerStepForVillains() throws InterruptedException {
        Map<String, JobParameter> parameterMap = new HashMap<>();
        parameterMap.put("isVillain", new JobParameter("true"));
        parameterMap.put("uniqueValue", new JobParameter(System.currentTimeMillis()));
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("avenger-step", new JobParameters(parameterMap));
        while (!jobExecution.getExitStatus().getExitCode().equals(ExitStatus.COMPLETED.getExitCode())) {
            Thread.sleep(100);
            jobExecution = jobExplorer.getJobExecution(jobExecution.getId());
        }

        Collection<StepExecution> stepExecutions =
                jobExecution.getStepExecutions();
        assertNotNull(stepExecutions);
        assertEquals(1, stepExecutions.size());
        assertTrue(stepExecutions.stream().allMatch(s -> s.getStepName().equals("avenger-step")));
        StepExecution stepExecution = stepExecutions.stream().filter(s -> s.getStepName().equals("avenger-step")).findFirst().get();
        assertEquals(villainCharacters.size(), stepExecution.getReadCount());
        long noOfAvengerVillains = villainCharacters.stream().filter(e -> e.getUniverse().equals("Avenger")).count();
        assertEquals(villainCharacters.size() - noOfAvengerVillains, stepExecution.getFilterCount());
        assertEquals(noOfAvengerVillains, stepExecution.getWriteCount());
    }

    @Test
    public void testAvengerStepForHeroes() throws InterruptedException {
        Map<String, JobParameter> parameterMap = new HashMap<>();
        parameterMap.put("isVillain", new JobParameter("false"));
        parameterMap.put("uniqueValue", new JobParameter(System.currentTimeMillis()));
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("avenger-step", new JobParameters(parameterMap));
        while (!jobExecution.getExitStatus().getExitCode().equals(ExitStatus.COMPLETED.getExitCode())) {
            Thread.sleep(100);
            jobExecution = jobExplorer.getJobExecution(jobExecution.getId());
        }

        Collection<StepExecution> stepExecutions =
                jobExecution.getStepExecutions();
        assertNotNull(stepExecutions);
        assertEquals(1, stepExecutions.size());
        assertTrue(stepExecutions.stream().allMatch(s -> s.getStepName().equals("avenger-step")));
        StepExecution stepExecution = stepExecutions.stream().filter(s -> s.getStepName().equals("avenger-step")).findFirst().get();
        assertEquals(heroCharacters.size(), stepExecution.getReadCount());
        long noOfAvengerHeroes = heroCharacters.stream().filter(e -> e.getUniverse().equals("Avenger")).count();
        assertEquals(heroCharacters.size() - noOfAvengerHeroes, stepExecution.getFilterCount());
        assertEquals(noOfAvengerHeroes, stepExecution.getWriteCount());
    }
}
