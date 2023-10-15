package link.go2.urlshortener.controllers;

import link.go2.urlshortener.config.AppConfig;
import link.go2.urlshortener.entities.UrlEntity;
import link.go2.urlshortener.entities.UrlResponseEntity;
import link.go2.urlshortener.services.UrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.net.URI;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class AppController {
    private final UrlService urlService;
    private final AppConfig config;

    @GetMapping
    public UrlResponseEntity getOriginalUrl(@RequestParam("url") String shortUrl) {
        try {
            if (shortUrl == null || shortUrl.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'url' is not provided");
            }

            if (log.isDebugEnabled()) {
                log.debug("Request to get full url has been received. Short url: {}", shortUrl);
            }

            if (!shortUrl.startsWith(config.getBaseUrl())) {
                if (log.isDebugEnabled()) {
                    log.debug("Alien url has been received: {}. Skip it", shortUrl);
                }
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong url");
            }

            return new UrlResponseEntity(urlService.getFullUrl(shortUrl).getOriginalUrl());
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("url/add")
    public UrlResponseEntity createShortUrl(@RequestBody UrlEntity urlEntity) {
        log.info("Request to create short url has been received. Original url: {}", urlEntity.getOriginalUrl());
        try {
            URI uri = URI.create(urlEntity.getOriginalUrl());
            uri.toURL();

            return new UrlResponseEntity(urlService.createShortUrl(urlEntity).getShortUrl());
        } catch (NullPointerException e) {
            log.warn("Request to create short url has been received, but original url wal empty");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'originalUrl' is not provided");
        } catch (IllegalArgumentException e) {
            log.warn("Request to create short url has been received, but given string is not a valid URL");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given string is not a valid URL");
        } catch (MalformedURLException e) {
            log.warn("Request to create short url has been received, but given protocol is invalid");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid protocol");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
