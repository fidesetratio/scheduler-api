package com.scheduler.app.job;

import java.net.URI;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Component
public class JenkinsSchedulerJob  extends QuartzJobBean {
	private static final Logger logger = LoggerFactory.getLogger(JenkinsSchedulerJob.class);
	private String url = "http://patartimotius:evievi123@localhost:8080/job/startjob/build";
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		try {
        logger.info("Executing Job with key {}", context.getJobDetail().getKey());
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        
        String jobName = jobDataMap.getString("jobname");
        
        
        
			/*
			 * URI uri = new URI(buffer.toString()); RestTemplate template = new
			 * RestTemplate(); ResponseEntity<String> result = template.postForEntity(uri,
			 * null, String.class);
			 * 
			 */String result = buildJob(jobName);
		logger.info("result:"+result);
		}catch(Exception e) {
			
			logger.info("error:"+e.getMessage());
		}finally {
			
		}
	}
	
	public String buildJob(String jobName) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("http://localhost:8080/");
		buffer.append("job/");
		buffer.append(jobName);
		buffer.append("/build");
		
		String url = buffer.toString();
		Client client = Client.create();
		client.addFilter(new com.sun.jersey.api.client.filter.HTTPBasicAuthFilter("patartimotius", "evievi123"));
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.post(ClientResponse.class);
		String jsonResponse = response.getEntity(String.class);
		client.destroy();
		
		return jsonResponse;

		
	}

}
