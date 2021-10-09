package models

import uuid "github.com/satori/go.uuid"

type User struct {
	Id       *uuid.UUID `db:"id"`
	Name     string     `db:"name"`
	Password string     `db:"password"`
}
