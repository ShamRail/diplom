CREATE TABLE if not exists users (
    id    uuid PRIMARY KEY,
    name  varchar(30) NOT NULL,
    password varchar(30)
);