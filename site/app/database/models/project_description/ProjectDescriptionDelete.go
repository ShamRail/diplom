package project_description

import uuid "github.com/satori/go.uuid"

type ProjectDescriptionDelete struct {
	Ids []uuid.UUID `json:"ids"`
}
