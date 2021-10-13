CREATE TABLE if not exists project_docs (
    id    uuid PRIMARY KEY,
    name  varchar(600),
    source_code_url varchar(600),
    build_command varchar(600),
    run_command varchar(600),
    in_files varchar(600) not null,
    out_files varchar(600) not null,
    configuration_id int,
    build_status varchar(100),
    archive_inner_dir varchar(600)
);