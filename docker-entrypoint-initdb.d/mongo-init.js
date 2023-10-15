print('Start #################################################################');

db = db.getSiblingDB('urls_db');
db.createUser(
    {
        user: 'admin',
        pwd: 'superpassword',
        roles: [{ role: 'readWrite', db: 'urls_db' }],
    },
);
db.createCollection('url');

db = db.getSiblingDB('urls_db_test');
db.createUser(
    {
        user: 'admin',
        pwd: 'superpassword',
        roles: [{ role: 'readWrite', db: 'urls_db_test' }],
    },
);
db.createCollection('url');
db.url.insertOne(
    {
        "_id":"652be4cb0593d6758126ed8c",
        "_class":"link.go2.urlshortener.entities.UrlEntity",
        "originalUrl":"https://google.com",
        "shortUrl":"https://go2.link/YqqWN"
    }
);

print('END #################################################################');