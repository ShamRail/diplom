package models

import uuid "github.com/satori/go.uuid"

type UserDelete struct {
	Ids       []uuid.UUID `db:"id"`
	Names     []string    `db:"name"`
	Passwords []string    `db:"password"`
}
