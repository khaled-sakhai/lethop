package com.example.springsocial.tasks;

import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class DailyTasks {

    private final CacheManager cacheManager;


    // remove cache after 3hours
    @Scheduled(fixedRate = 10800000)
    public void evictCachesAtIntervals() {
        evictAllCaches();
    }



    // helper
    private void evictAllCaches() {
        cacheManager.getCacheNames()
                .forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
    }
}
