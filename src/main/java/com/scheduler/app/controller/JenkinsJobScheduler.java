package com.scheduler.app.controller;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

import javax.validation.Valid;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scheduler.app.job.EmailJob;
import com.scheduler.app.job.JenkinsSchedulerJob;
import com.scheduler.app.payload.JenkinsPayloadRequest;
import com.scheduler.app.payload.JenkinsPayloadResponse;
import com.scheduler.app.payload.ScheduleEmailRequest;
import com.scheduler.app.payload.ScheduleEmailResponse;

@RestController
public class JenkinsJobScheduler {

	@Autowired
    private Scheduler scheduler;
	
	 @PostMapping("/scheduleJenkinsJob")
	 public ResponseEntity<JenkinsPayloadResponse> scheduleJenkinsJob(@Valid @RequestBody JenkinsPayloadRequest jenkinsPayloadRequest) {
		 
		 try {
	            ZonedDateTime dateTime = ZonedDateTime.of(jenkinsPayloadRequest.getDateTime(), jenkinsPayloadRequest.getTimeZone());
	            System.out.println(dateTime);
	            System.out.println("m"+dateTime.getMonth());

	            System.out.println("d"+dateTime.getDayOfMonth());
	            
	            if(dateTime.isBefore(ZonedDateTime.now())) {
	            	System.out.println(dateTime);
	            	JenkinsPayloadResponse jenkinsResponse = new JenkinsPayloadResponse("dateTime must be after current time");
	                return ResponseEntity.badRequest().body(jenkinsResponse);
	            }
	            	JobDetail jobDetail = buildJobDetail(jenkinsPayloadRequest);
	                Trigger trigger = buildJobTrigger(jobDetail, dateTime);
	                scheduler.scheduleJob(jobDetail, trigger);

	                JenkinsPayloadResponse jenkinsResponse = new JenkinsPayloadResponse( "Email Scheduled Successfully!");
	                return ResponseEntity.ok(jenkinsResponse);
	            
		 } catch (SchedulerException ex) {
	 
			 JenkinsPayloadResponse error = new JenkinsPayloadResponse("Error scheduling email. Please try later!");
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	        }
	 
	 }
	   
		 
		 private JobDetail buildJobDetail(JenkinsPayloadRequest jenkinsRequest) {
		        JobDataMap jobDataMap = new JobDataMap();

		        jobDataMap.put("jobname", jenkinsRequest.getName());
			        return JobBuilder.newJob(JenkinsSchedulerJob.class)
		                .withIdentity(UUID.randomUUID().toString(), "jenkins-jobs")
		                .withDescription("Send Jenkins Job")
		                .usingJobData(jobDataMap)
		                .storeDurably()
		                .build();
		    }
		 
		 private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
		        return TriggerBuilder.newTrigger()
		                .forJob(jobDetail)
		                .withIdentity(jobDetail.getKey().getName(), "email-jenkins")
		                .withDescription("Send Jenkins Trigger")
		                .startAt(Date.from(startAt.toInstant()))
		                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
		                .build();
		    }


}
