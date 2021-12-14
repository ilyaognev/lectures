-- file: 10-create-user-and-db.sql
CREATE DATABASE simple_backend;
CREATE ROLE program WITH PASSWORD 'test';
GRANT ALL PRIVILEGES ON DATABASE simple_backend TO program;
ALTER ROLE program WITH LOGIN;