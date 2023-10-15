package link.go2.urlshortener.services;

import link.go2.urlshortener.config.AppConfig;
import link.go2.urlshortener.entities.UrlEntity;
import link.go2.urlshortener.repositiries.UrlRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrlService {
    private final AppConfig config;
    private final CacheService cacheService;
    private final UrlRepository urlRepository;
    private static final UrlEntity EMPTY_URL_ENTITY = new UrlEntity();
    private static final char[] chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public UrlEntity getFullUrl(@NonNull String shortUrl) {
        UrlEntity result = (UrlEntity) cacheService.getFromCache(shortUrl);
        if (result != null) {
            if (log.isDebugEnabled()) {
                log.debug("Returning from cache: {}", result);
            }
            return result;
        }

        if (log.isDebugEnabled()) {
            log.debug("Cache is empty. Trying to find in database");
        }
        result = urlRepository.findUrlEntityByShortUrl(shortUrl);
        if (log.isDebugEnabled()) {
            log.debug("Result from database: {}", result);
        }

        if (result == null) {
            return EMPTY_URL_ENTITY;
        }

        updateCache(result);
        return result;
    }

    public UrlEntity createShortUrl(UrlEntity urlEntity) {
        UrlEntity findUrlEntity = (UrlEntity) cacheService.getFromCache(urlEntity.getOriginalUrl());
        if (findUrlEntity != null) {
            if (log.isDebugEnabled()) {
                log.debug("Returning from cache: {}", findUrlEntity);
            }
            return findUrlEntity;
        }

        findUrlEntity = urlRepository.findUrlEntityByOriginalUrl(urlEntity.getOriginalUrl());
        if (findUrlEntity != null) {
            if (log.isDebugEnabled()) {
                log.debug("Result from database: {}", findUrlEntity);
            }
            return findUrlEntity;
        }

        String randomStr = createRandomStr(config.getUrlLength());
        urlEntity.setShortUrl(config.getBaseUrl() + randomStr);
        if (log.isDebugEnabled()) {
            log.debug("Short url '{}' has been generated", urlEntity.getShortUrl());
        }
        urlRepository.save(urlEntity);
        if (log.isDebugEnabled()) {
            log.debug("{} has been saved to database", urlEntity);
        }

        updateCache(urlEntity);

        log.info("Short url '{}' from original url '{}' has been created successfully", urlEntity.getShortUrl(), urlEntity.getOriginalUrl());
        return urlEntity;
    }

    private void updateCache(UrlEntity urlEntity) {
        cacheService.addToCache(urlEntity.getOriginalUrl(), urlEntity);
        cacheService.addToCache(urlEntity.getShortUrl(), urlEntity);
        if (log.isDebugEnabled()) {
            log.debug("{} has been cached successfully", urlEntity);
        }
    }

    private String createRandomStr(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }

        return sb.toString();
    }
}
