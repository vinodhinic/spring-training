package com.batch;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class CustomJobParameterConverter implements JobParametersConverter {

    @Override
    public JobParameters getJobParameters(Properties properties) {
        Map<String, JobParameter> parameterMap = new HashMap<>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String keyStr = (String) entry.getKey();
            if (keyStr.contains("isVillain") ) {
                parameterMap.put(
                        "isVillain", new JobParameter(entry.getValue().toString()));
            }
        }
        parameterMap.put("uniqueValue", new JobParameter(System.currentTimeMillis()));
        return new JobParameters(parameterMap);
    }

    @Override
    public Properties getProperties(JobParameters params) {
        return params.toProperties();
    }
}
