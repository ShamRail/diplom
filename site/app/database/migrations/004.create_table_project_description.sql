CREATE TABLE if not exists project_description (
       id          uuid PRIMARY KEY,
       project_id  uuid not null,
       name        VARCHAR (100),
       author      VARCHAR (100),
       short_description VARCHAR(100) not null,
       description VARCHAR(500),
       project_status int
);