package com.project.rate_limiter.entity;

import java.util.Deque;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRequestInfo {
	private long limitWindowStart;
	private int numberOfRequestsMade;
	private Deque<Long> requestList;
}
