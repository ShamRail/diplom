CREATE TABLE if not exists users (
    id    uuid PRIMARY KEY,
    name  varchar(100) NOT NULL,
    email  varchar(100) NOT NULL,
    password bytea NOT NULL
);