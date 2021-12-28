CREATE TABLE if not exists project_description (
       id          uuid PRIMARY KEY,
       project_id  serial not null,
       author       VARCHAR(100) not null,
       user_id      uuid not null,
       short_description VARCHAR(100) not null,
       description VARCHAR(500),
       project_status int
);