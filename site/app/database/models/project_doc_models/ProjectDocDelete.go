package project_doc_models

import uuid "github.com/satori/go.uuid"

type ProjectDocDelete struct {
	Ids []uuid.UUID `json:"ids"`
}
