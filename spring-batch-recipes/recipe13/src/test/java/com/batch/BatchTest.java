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

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestAppConfig.class})
public class BatchTest {

    @Autowired
    @Qualifier("simpleJob")
    private Job simpleJob;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobExplorer jobExplorer;

    @Before
    public void setup() {
        jobLauncherTestUtils.setJob(simpleJob);
    }

    @Test
    public void testSimpleJobForEvenRunId() throws Exception {
        Map<String, JobParameter> parameterMap = new HashMap<>();
        parameterMap.put("run.id", new JobParameter(2L));
        parameterMap.put("uniqueValue", new JobParameter(System.currentTimeMillis()));
        JobExecution jobExecution = jobLauncherTestUtils.launchJob( new JobParameters(parameterMap));
        while (!jobExecution.getExitStatus().getExitCode().equals(ExitStatus.COMPLETED.getExitCode())) {
            Thread.sleep(100);
            jobExecution = jobExplorer.getJobExecution(jobExecution.getId());
        }

        Collection<StepExecution> stepExecutions =
                jobExecution.getStepExecutions();
        assertNotNull(stepExecutions);
        assertEquals(1, stepExecutions.size());
        assertTrue(stepExecutions.stream().allMatch(s -> s.getStepName().equals("simple-even-step")));
        StepExecution stepExecution = stepExecutions.stream().filter(s -> s.getStepName().equals("simple-even-step")).findFirst().get();
        assertEquals(stepExecution.getExitStatus().getExitCode(), ExitStatus.COMPLETED.getExitCode());
    }

    @Test
    public void testSimpleJobForOddRunId() throws Exception {
        Map<String, JobParameter> parameterMap = new HashMap<>();
        parameterMap.put("run.id", new JobParameter(1L));
        parameterMap.put("uniqueValue", new JobParameter(System.currentTimeMillis()));
        JobExecution jobExecution = jobLauncherTestUtils.launchJob( new JobParameters(parameterMap));
        while (!jobExecution.getExitStatus().getExitCode().equals(ExitStatus.COMPLETED.getExitCode())) {
            Thread.sleep(100);
            jobExecution = jobExplorer.getJobExecution(jobExecution.getId());
        }

        Collection<StepExecution> stepExecutions =
                jobExecution.getStepExecutions();
        assertNotNull(stepExecutions);
        assertEquals(1, stepExecutions.size());
        assertTrue(stepExecutions.stream().allMatch(s -> s.getStepName().equals("simple-odd-step")));
        StepExecution stepExecution = stepExecutions.stream().filter(s -> s.getStepName().equals("simple-odd-step")).findFirst().get();
        assertEquals(stepExecution.getExitStatus().getExitCode(), ExitStatus.COMPLETED.getExitCode());
    }
}