package link.go2.urlshortener.repositiries;

import link.go2.urlshortener.entities.UrlEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlRepository extends MongoRepository<UrlEntity, String> {
    UrlEntity findUrlEntityByShortUrl(String shortUrl);
    UrlEntity findUrlEntityByOriginalUrl(String originalUrl);
}
