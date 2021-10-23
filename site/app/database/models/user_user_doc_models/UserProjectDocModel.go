package user_user_doc_models

import (
	uuid "github.com/satori/go.uuid"
)

type UserUserDoc struct {
	Id        *uuid.UUID `db:"id"`
	UserId    *uuid.UUID `db:"user_id"`
	ProjectId *uuid.UUID `db:"project_id"`
}
