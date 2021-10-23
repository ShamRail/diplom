package user_user_doc_models

import uuid "github.com/satori/go.uuid"

type UserProjectDocDelete struct {
	Ids []uuid.UUID `json:"ids"`
}
