package intents

type ProjectDocUpdateIntent struct {
	Id string `json:"id"`

	Name          string `json:"name"`
	SourceCodeUrl string `json:"source_code_url"`
	BuildCommand  string `json:"build_command"`
	RunCommand    string `json:"run_command"`
	InFiles       string `json:"in_files"`
	OutFiles      string `json:"out_files"`

	BuildStatus     string `json:"build_status"`
	ArchiveInnerDir string `json:"archive_inner_dir"`

	Description string `json:"description"`
}
