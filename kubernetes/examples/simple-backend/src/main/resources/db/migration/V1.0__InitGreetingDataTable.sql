-- 1.0 Inti greeting_data table and insert data
CREATE TABLE greeting_data
(
    id      SERIAL PRIMARY KEY,
    person  VARCHAR(80)  NOT NULL,
    message VARCHAR(255) NOT NULL
);

INSERT INTO greeting_data(person, message)
VALUES ('docker', 'Hello from docker container'),
       ('Alex', 'Good evening, Alex')