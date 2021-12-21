INSERT INTO project_docs (name, source_code_url, build_command, run_command, in_files, out_files,
                                 configuration_id, archive_inner_dir)
VALUES ('TestName', '/url', '-build', '-run', '/app', '/out', 1, 'test_inner'),
       ('TestName2', '/url', '-build', '-run', '/app', '/out', 1,'test_inner2');