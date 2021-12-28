package user_project_models

import uuid "github.com/satori/go.uuid"

type UserProjectFilter struct {
	Ids        []uuid.UUID `db:"id"`
	UserIds    []uuid.UUID `db:"user_id"`
	ProjectIds []int       `db:"project_id"`
}
