package xyz.melodyl.pojo;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Component
public class UserCache {
    private Cache<String, User> cache;

    @Value("${cache.expiration:30}")
    private int expireTime;

    @Value("${cache.cacheSize:1024}")
    private int maxCacheSize;

    public UserCache(){}

    @PostConstruct
    public void setUp() {
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expireTime, TimeUnit.MINUTES)
                .maximumSize(maxCacheSize)
                .build();

    }

    @PreDestroy
    public void tearDown() {
        this.cache.cleanUp();
    }

    public void putUser(String key, User user) {
        cache.put(key, user);
    }

    public User getUserIfExist(String key) {
        return cache.getIfPresent(key);
    }
}
