# RESTful

## Тестовые данные

Есть две тестовые таблицы Сервера и Страны:

```sql
CREATE TABLE states
(
    id      SERIAL PRIMARY KEY,
    city    VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL
);

CREATE TABLE servers
(
    id        SERIAL PRIMARY KEY,
    address   VARCHAR(255),
    purpose   VARCHAR(20) NOT NULL,
    latency   INT         NOT NULL,
    bandwidth INT         NOT NULL,
    state_id  INT         NOT NULL
        CONSTRAINT fk_servers_state_id REFERENCES states (id)
);
```

Приложение делится на две части:

* RESTful API (/api/v1/**): [ServerRestController](src/main/java/ru/romanow/simple/web/ServerRestController.kt),
  [StateRestController](src/main/java/ru/romanow/simple/web/StateRestController.kt).  
  Реализует CRUD операции над ресурсами Server и State.
* HATEOAS (/hateoas/v1/**):
  [HateoasServerRestController](src/main/java/ru/romanow/simple/web/HateoasServerRestController.kt),
  [HateoasStateRestController](src/main/java/ru/romanow/simple/web/HateoasStateRestController.kt).  
  Реализует только _операции на чтение_ над ресурсами Server и State.

Для RESTful генерируется OpenAPI, для его просмотра используется Swagger UI, который доступен по
адресу `http://localhost:8080/swagger-ui.html`.

Для навигации по HATEOAS используется HAL
browser `docker run -e ENTRY_POINT=http://localhost:8080/hateoas/ -p 8081:80 jcassee/hal-browser`. Т.к. запрос
выполняется с origin отличного от того, где запущен сервер, включен CORS.

### SSL

```shell
openssl req -newkey rsa:2048 -nodes -keyout key.pem -x509 -days 365 -out certificate.pem
openssl pkcs12 -inkey key.pem -in certificate.pem -export -out certificate.p12
curl https://localhost:8443/api/v1/state -v -k | jq

* TCP_NODELAY set
* Connected to localhost (::1) port 8443 (#0)
* ALPN, offering h2
* ALPN, offering http/1.1
* successfully set certificate verify locations:
*   CAfile: /etc/ssl/cert.pem
  CApath: none
  ...
* SSL connection using TLSv1.2 / ECDHE-RSA-AES256-GCM-SHA384
* ALPN, server did not agree to a protocol
* Server certificate:
*  subject: C=RU; ST=Moscow
*  start date: Oct 21 12:18:59 2019 GMT
*  expire date: Oct 20 12:18:59 2020 GMT
*  issuer: C=RU; ST=Moscow
*  SSL certificate verify result: self signed certificate (18), continuing anyway.
> GET /api/v1/state HTTP/1.1
> Host: localhost:8443
> User-Agent: curl/7.64.1
> Accept: */*
> 
< HTTP/1.1 200 
< ETag: "649169dd7e1ab6ed1cfd5b1103855621"
< Cache-Control: max-age=60
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Wed, 15 Sep 2021 13:13:58 GMT

* Closing connection 0
{
  "states": [
    {
      "id": 1,
      "city": "Moscow",
      "country": "Russia"
    },
    {
      "id": 2,
      "city": "SPb",
      "country": "Russia"
    }
  ]
}
```

### HTTP/2

```shell
curl http://localhost:8880/api/v1/state --http2 -v

> GET /api/v1/state HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.64.1
> Accept: */*
> Connection: Upgrade, HTTP2-Settings
> Upgrade: h2c
> HTTP2-Settings: AAMAAABkAARAAAAAAAIAAAAA
> 
< HTTP/1.1 101 
< Connection: Upgrade
< Upgrade: h2c
< Date: Wed, 15 Sep 2021 13:11:48 GMT
* Received 101
* Using HTTP2, server supports multi-use
* Connection state changed (HTTP/2 confirmed)
* Copying HTTP/2 data in stream buffer to connection buffer after upgrade: len=15
* Connection state changed (MAX_CONCURRENT_STREAMS == 100)!
< HTTP/2 200 
< etag: "649169dd7e1ab6ed1cfd5b1103855621"
< cache-control: max-age=60
< content-type: application/json
< date: Wed, 15 Sep 2021 13:11:48 GMT

* Closing connection 0
{
  "states": [
    {
      "id": 1,
      "city": "Moscow",
      "country": "Russia"
    },
    {
      "id": 2,
      "city": "SPb",
      "country": "Russia"
    }
  ]
}
```

### NGINX кэширование

Запустить два инстанса на разных портах:

```shell script
java -jar build/libs/restful.jar --server.port=8081
java -jar build/libs/restful.jar --server.port=8082
```

Конфигурация nginx:

```
upstream api {
 server 127.0.0.1:8081 max_fails=3 weight=5;
 server 127.0.0.1:8082 backup;
}

server {
 listen 80;
 server_name test.balance.local;

 location / {
   proxy_set_header Host $host;
   proxy_set_header X-Real-IP $remote_addr;
   proxy_pass http://api;
   proxy_redirect off;
 }
}
```

### NGINX балансировка

Конфигурация nginx:

```
proxy_cache_path /var/cache/nginx levels=1:2 keys_zone=STATIC:32m max_size=1g;

server {
 listen 80;
 server_name test.cache.local;

 location / {
  proxy_cache STATIC;
  proxy_cache_valid any 48h;
  add_header X-Cached $upstream_cache_status;

  proxy_set_header Host $host;
      proxy_set_header X-Real-IP $remote_addr;
      proxy_pass http://127.0.0.1:8080;
      proxy_redirect off;
 }
}
```

Два раза выполнить запрос через curl:

```shell script
curl http://test.cache.local/api/server/1 -v
```

Второй раз в ответ получим заголовок `X-Cached: HIT`, т.е. сервер ответил 302, а тело запроса nginx достал из кэша.  
Для метода `http://localhost:8880/api/state` мы отдаем заголовок `Cache-Control: 60` (повторно выполнить запрос через 1
минуту) и `ETag`, на базе которого строится кэширование. Для запроса `http://localhost:8880/api/state/1` устанавливается
заголовок `Cache-Control: no-cahce`, который указывает промежуточным прокси, что запрос нельзя кэшировать, а нужно
перезапрашивать каждый раз.

## Сборка

```shell
docker compose up -d
./gradlew clean build bootRun
```