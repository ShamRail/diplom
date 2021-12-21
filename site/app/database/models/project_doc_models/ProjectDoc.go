package project_doc_models

type ProjectDoc struct {
	Id              int    `db:"id" json:"id"`
	Name            string `db:"name" json:"name"`
	SourceCodeUrl   string `db:"source_code_url" json:"sourceCodeURL"`
	BuildCommand    string `db:"build_command" json:"buildCommand"`
	RunCommand      string `db:"run_command" json:"runCommand"`
	InFiles         string `db:"in_files" json:"inFiles"`
	OutFiles        string `db:"out_files" json:"outFiles"`
	ConfigurationId int    `db:"configuration_id" json:"configID"`
	ArchiveInnerDir string `db:"archive_inner_dir" json:"archiveInnerDir"`
}
