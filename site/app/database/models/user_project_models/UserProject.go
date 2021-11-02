package user_project_models

import (
	uuid "github.com/satori/go.uuid"
)

type UserProject struct {
	Id        *uuid.UUID `db:"id"`
	UserId    *uuid.UUID `db:"user_id"`
	ProjectId *uuid.UUID `db:"project_id"`
}
