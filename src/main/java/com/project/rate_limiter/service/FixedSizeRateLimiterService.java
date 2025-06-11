package com.project.rate_limiter.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.rate_limiter.entity.UserRequestInfo;

@Service
public class FixedSizeRateLimiterService {
	
	@Value("${rate.request.limit.count}")
	private int REQUEST_LIMIT;
	
	@Value("${rate.request.limit.timeperiod}")
	private long TIME_WINDOW_MS;
	
	private Map<String, UserRequestInfo> userRequestMap = new HashMap<>();
	
	public boolean isAllowed(String user) {
		long currentTime = Instant.now().toEpochMilli();
		UserRequestInfo userInfo =  userRequestMap.getOrDefault(user, new UserRequestInfo(currentTime, 0, null));
		
		if(currentTime - userInfo.getLimitWindowStart() > TIME_WINDOW_MS) {
			userInfo.setNumberOfRequestsMade(1);
			userInfo.setLimitWindowStart(currentTime);
			userRequestMap.put(user, userInfo);
			
			return true;
		}
		
		if(userInfo.getNumberOfRequestsMade() < REQUEST_LIMIT) {
			userInfo.setNumberOfRequestsMade(userInfo.getNumberOfRequestsMade()+1);
			userRequestMap.put(user, userInfo);
			
			return true;
		}
		
		return false;
	}
}