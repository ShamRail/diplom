package user_project_models

import uuid "github.com/satori/go.uuid"

type UserProjectFilter struct {
	Ids        []uuid.UUID `db:"id"`
	UserIds    []uuid.UUID `db:"user_id"`
	ProjectIds []uuid.UUID `db:"project_id"`
}
