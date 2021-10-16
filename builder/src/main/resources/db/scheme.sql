create table project
(
    id                integer primary key,
    name              text,
    source_code_url   text,
    build_command     text,
    run_command       text,
    image_id          text,
    in_files          text,
    out_files         text,
    configuration_id  integer,
    build_status      text,
    archive_inner_dir text
);

create table build
(
    id              serial primary key,
    build_status    text,
    message         text,
    start_at        timestamp,
    end_at          timestamp,
    log_path        text,
    docker_log_path text,
    project_id      int references project (id)
);

