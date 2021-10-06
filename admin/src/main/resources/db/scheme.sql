create table language
(
    id      serial primary key,
    name    text,
    version text,
    logo    text
);

create table builder
(
    id      serial primary key,
    name    text,
    version text,
    logo    text
);

create table language_builder
(
    id          serial primary key,
    language_id int references language (id),
    builder_id  int references builder (id)
);

create table configuration
(
    id          serial primary key,
    name        text,
    version     text,
    description text,
    docker_file text,
    build_id    int references builder (id),
    language_id int references language (id)
);