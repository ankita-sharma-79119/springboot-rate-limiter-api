package com.project.rate_limiter.service;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.rate_limiter.entity.UserRequestInfo;

@Service
public class SlidingWindowRateLimiterService {
	
	@Value("${rate.request.limit.count}")
	private int REQUEST_LIMIT;
	
	@Value("${rate.request.limit.timeperiod}")
	private long TIME_WINDOW_MS;
	
	Map<String, UserRequestInfo> userRequestMap = new HashMap<>();
	
	public boolean isAllowed(String user) {
		long currentTime = Instant.now().toEpochMilli();
		
		UserRequestInfo userInfo = userRequestMap.getOrDefault(user, 
				new UserRequestInfo(currentTime, 0, new ArrayDeque<>()));
		
		userInfo.getRequestList().removeIf(val -> currentTime - val > TIME_WINDOW_MS);
		
		if(userInfo.getRequestList().size() >= REQUEST_LIMIT) {
			return false;
		}
		
		userInfo.getRequestList().addLast(currentTime);
		userInfo.setNumberOfRequestsMade(userInfo.getNumberOfRequestsMade()+1);
		userRequestMap.put(user, userInfo);
		
		return true;
	}
}