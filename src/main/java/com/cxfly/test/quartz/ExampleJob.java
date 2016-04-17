package com.cxfly.test.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class ExampleJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(ExampleJob.class);
    
    protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
        logger.info("Job is run...");
    }

}
