-- file: 10-create-user-and-db.sql
CREATE DATABASE restful;
CREATE ROLE program WITH PASSWORD 'test';
GRANT ALL PRIVILEGES ON DATABASE restful TO program;
ALTER ROLE program WITH LOGIN;