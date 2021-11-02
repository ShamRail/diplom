package user_project_models

import uuid "github.com/satori/go.uuid"

type UserProjectDelete struct {
	Ids []uuid.UUID `json:"ids"`
}
