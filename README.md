# go2.link
>  URL shortener service prototype

- Java 17
- Spring boot / Spring Data
- REST-based service
- Mongodb + Redis
- Docker compose
- Tests included

## Public API
> REST API BaseURL: localhost:8080/api/v1
### Create short URL
> POST url/add

**Request body:**

```
{
    "originalUrl": "https://google.com"
}
```

**Response:**

```
{
    "result": "https://go2.link/YX8Bj"
}
```

### Get original URL
> GET ?url=https://go2.link/YX8Bj

**Response:**

```
{
    "result": "https://google.com"
}
```

## Launch service

- `docker-compose up`
- `mvn clean package`
- `java -jar target/url-shortener-0.0.1-SNAPSHOT.jar`