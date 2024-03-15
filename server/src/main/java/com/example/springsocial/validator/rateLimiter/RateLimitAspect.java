package com.example.springsocial.validator.rateLimiter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class RateLimitAspect {


    public static final String ERROR_MESSAGE = "Too many requests at endpoint %s from IP %s! Please try again after %d milliseconds!";
    private final ConcurrentHashMap<String, List<Long>> requestCounts = new ConcurrentHashMap<>();

    @Value("${app.rate.limit:#{200}}")
    private int rateLimit;

    @Value("${app.rate.durationinms:#{60000}}")
    private long rateDuration;

    /**
     * Executed before any method within a class annotated with {@link WithRateLimitProtection}.
     * Counts calls per remote address. Calls older than {@link #rateDuration} milliseconds will be forgotten.
     * If there have been more than {@link #rateLimit} calls within {@link #rateDuration} milliseconds
     * from a remote address, a {@link RateLimitException} will be thrown.
     * @throws RateLimitException iff rate limit for a given remote address has been exceeded
     */
    @Before("@within(rateLimitedAnnotation) && execution(* *(..))")
    public void rateLimitBeforeClass(WithRateLimitProtection rateLimitedAnnotation) {
        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final String key = requestAttributes.getRequest().getRemoteAddr();
        final long currentTime = System.currentTimeMillis();
        requestCounts.putIfAbsent(key, new ArrayList<>());
        requestCounts.get(key).add(currentTime);
        cleanUpRequestCounts(currentTime);
        if (requestCounts.get(key).size() > rateLimit) {
            throw new RateLimitException(String.format(ERROR_MESSAGE, requestAttributes.getRequest().getRequestURI(), key, rateDuration));
        }
    }

    private void cleanUpRequestCounts(final long currentTime) {
        requestCounts.values().forEach(l -> {
            l.removeIf(t -> timeIsTooOld(currentTime, t));
        });
    }

    private boolean timeIsTooOld(final long currentTime, final long timeToCheck) {
        return currentTime - timeToCheck > rateDuration;
    }

}