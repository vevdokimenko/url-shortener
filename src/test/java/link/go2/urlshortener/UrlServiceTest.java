package link.go2.urlshortener;

import link.go2.urlshortener.config.AppConfig;
import link.go2.urlshortener.entities.UrlEntity;
import link.go2.urlshortener.services.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class UrlServiceTest {
    @Autowired private UrlService urlService;
    @Autowired AppConfig config;

    @Test
    void should_ReturnUrlEntity_when_getFullUrlCorrect() {
        UrlEntity fullUrl = urlService.getFullUrl("https://go2.link/YqqWN");
        assertNotNull(fullUrl);

        String originalUrl = fullUrl.getOriginalUrl();
        String shortUrl = fullUrl.getShortUrl();

        assertNotNull(originalUrl);
        assertNotNull(shortUrl);

        assertEquals("https://google.com", originalUrl);
        assertEquals("https://go2.link/YqqWN", shortUrl);
    }

    @Test
    void should_ReturnUrlEntity_when_createShortUrlCorrect() {
        UrlEntity entity = new UrlEntity();
        entity.setOriginalUrl("https://facebook.com");

        UrlEntity fullUrl = urlService.createShortUrl(entity);
        assertNotNull(fullUrl);

        String originalUrl = fullUrl.getOriginalUrl();
        String shortUrl = fullUrl.getShortUrl();

        assertNotNull(originalUrl);
        assertNotNull(shortUrl);

        assertEquals("https://facebook.com", originalUrl);
        assertTrue(shortUrl.startsWith(config.getBaseUrl()));
    }

    @Test
    void should_ReturnUrlEntity_when_getFullUrlUnknown() {
        UrlEntity fullUrl = urlService.getFullUrl("UNKNOWN");
        assertNotNull(fullUrl);

        assertNull(fullUrl.getOriginalUrl());
        assertNull(fullUrl.getShortUrl());
    }

    @Test
    void should_ReturnDifferentRandomStrings_when_createRandomStr() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?>[] parameterType = { int.class };
        Method method = UrlService.class.getDeclaredMethod("createRandomStr", parameterType);
        method.setAccessible(true);

        String randomStr = (String) method.invoke(urlService, config.getUrlLength());
        assertEquals(config.getUrlLength(), randomStr.length());

        String otherStr = (String) method.invoke(urlService, config.getUrlLength());
        assertEquals(config.getUrlLength(), randomStr.length());

        assertNotEquals(randomStr, otherStr);
    }
}
