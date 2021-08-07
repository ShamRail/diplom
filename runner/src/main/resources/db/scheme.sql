create table project
(
    id                integer primary key,
    name              text,
    run_command       text,
    image_id          text,
    in_files          text,
    out_files         text,
    build_status      text,
    configuration_id  integer
);

create table app
(
    id           serial primary key,
    log_path     text,
    container_id text,
    start_at     timestamp,
    end_at       timestamp,
    ws_uri       text,
    project_id   int references project (id),
    app_status   text,
    message      text
);
