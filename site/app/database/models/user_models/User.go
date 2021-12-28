package user_models

import uuid "github.com/satori/go.uuid"

type User struct {
	Id       *uuid.UUID `db:"id"`
	Name     string     `db:"name"`
	Email    string     `db:"email"`
	Password []byte     `db:"password"`
}

type UserIntent struct {
	Id       string `db:"id" json:"id"`
	Name     string `db:"name" json:"name"`
	Email    string `db:"email" json:"email"`
	Password string `db:"password" json:"password"`
}
