package com.scheduler.app.payload;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class JenkinsPayloadRequest {

	@NotEmpty
	private String name;
	
	 @NotNull
	 private LocalDateTime dateTime;


	    @NotNull
	    private ZoneId timeZone;

	public ZoneId getTimeZone() {
			return timeZone;
		}

		public void setTimeZone(ZoneId timeZone) {
			this.timeZone = timeZone;
		}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
