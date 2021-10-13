package user_models

import uuid "github.com/satori/go.uuid"

type UserFilter struct {
	Ids       []uuid.UUID `json:"ids"`
	Names     []string    `json:"names"`
	Passwords []string    `json:"passwords"`
}
