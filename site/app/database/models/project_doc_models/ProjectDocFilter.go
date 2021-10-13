package project_doc_models

import uuid "github.com/satori/go.uuid"

type ProjectDocFilter struct {
	Ids              []uuid.UUID `json:"ids"`
	Names            []string    `json:"names"`
	ConfigurationIds []int       `json:"configuration_ids"`
	BuildStatuses    []string    `json:"build_statuses"`
}
