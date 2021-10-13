package user_models

import uuid "github.com/satori/go.uuid"

type UserDelete struct {
	Ids []uuid.UUID `json:"ids"`
}
