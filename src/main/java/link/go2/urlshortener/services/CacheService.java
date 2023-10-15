package link.go2.urlshortener.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final RedisTemplate<String, Object> template;

    public void addToCache(String key, Object value) {
        template.opsForValue().set(key, value);
    }

    public Object getFromCache(String key) {
        return template.opsForValue().get(key);
    }

    public void removeFromCache(String key) {
        template.delete(key);
    }
}
