package user_models

import uuid "github.com/satori/go.uuid"

type User struct {
	Id       *uuid.UUID `db:"id"`
	Name     string     `db:"name"`
	Email    string     `db:"email"`
	Password []byte     `db:"password"`
}
