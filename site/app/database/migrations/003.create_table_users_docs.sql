CREATE TABLE if not exists user_project (
    id    uuid PRIMARY KEY,
    user_id  uuid not null,
    project_id serial not null
);