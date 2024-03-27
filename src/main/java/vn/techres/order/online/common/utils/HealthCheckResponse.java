package vn.techres.order.online.common.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class HealthCheckResponse {
	
	@JsonProperty("build_number")
	private String buildNumber;
	
	@JsonProperty("build_time")
	private String buildTime;
	
	public HealthCheckResponse(String buildNumber, String buildTime) {
		this.buildNumber = buildNumber;
		this.buildTime = buildTime;
	}
}
