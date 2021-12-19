package intents

import uuid "github.com/satori/go.uuid"

type ProjectDocIntent struct {
	UserId   *uuid.UUID `db:"id"`
	UserName string     `json:"userName"`

	Name            string `json:"name"`
	SourceCodeUrl   string `json:"source_code_url"`
	BuildCommand    string `json:"build_command"`
	RunCommand      string `json:"run_command"`
	InFiles         string `json:"in_files"`
	OutFiles        string `json:"out_files"`
	ConfigurationId int    `json:"configuration_id"`
	BuildStatus     string `json:"build_status"`
	ArchiveInnerDir string `json:"archive_inner_dir"`

	Description string `json:"description"`
}
