package user_user_doc_models

import uuid "github.com/satori/go.uuid"

type UserProjectDocFilter struct {
	Ids        []uuid.UUID `db:"id"`
	UserIds    []uuid.UUID `db:"user_id"`
	ProjectIds []uuid.UUID `db:"project_id"`
}
