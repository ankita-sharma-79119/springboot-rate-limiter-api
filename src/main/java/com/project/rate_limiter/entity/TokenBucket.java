package com.project.rate_limiter.entity;

import lombok.Data;

@Data
public class TokenBucket {
    int tokens;
    int capacity;
    int refillRate;
    long lastRefillTimestamp;

    public TokenBucket(int capacity, int refillRate, long lastRefillTimestamp) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefillTimestamp = lastRefillTimestamp;
    }

    public void refill(long currentTime) {
        long elapsedTime = currentTime - lastRefillTimestamp;
        long tokensToAdd = (elapsedTime / 1000) * refillRate;
        if (tokensToAdd > 0) {
            tokens = Math.min(capacity, tokens + (int) tokensToAdd);
            lastRefillTimestamp = currentTime;
        }
    }
}
