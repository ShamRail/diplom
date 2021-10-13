package project_doc_models

import uuid "github.com/satori/go.uuid"

type ProjectDoc struct {
	Id              *uuid.UUID `db:"id"`
	Name            string     `db:"name"`
	SourceCodeUrl   string     `db:"source_code_url"`
	BuildCommand    string     `db:"build_command"`
	RunCommand      string     `db:"run_command"`
	InFiles         string     `db:"in_files"`
	OutFiles        string     `db:"out_files"`
	ConfigurationId int        `db:"configuration_id"`
	BuildStatus     string     `db:"build_status"`
	ArchiveInnerDir string     `db:"archive_inner_dir"`
}
