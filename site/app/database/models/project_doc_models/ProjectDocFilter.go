package project_doc_models

type ProjectDocFilter struct {
	Ids              []int    `json:"ids"`
	Names            []string `json:"names"`
	ConfigurationIds []int    `json:"configuration_ids"`
	BuildStatuses    []string `json:"build_statuses"`
}
