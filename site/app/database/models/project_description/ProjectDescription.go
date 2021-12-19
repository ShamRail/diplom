package project_description

import uuid "github.com/satori/go.uuid"

type Status int

const (
	Completed  Status = 0
	Demo       Status = 1
	Deprecated Status = 3
)

type ProjectDescription struct {
	Id               *uuid.UUID `db:"id"`
	ProjectId        int        `db:"project_id"`
	UserId           *uuid.UUID `db:"user_id"`
	Author           string     `db:"author"`
	ShortDescription string     `db:"short_description"`
	Description      string     `db:"description"`
	ProjectStatus    Status     `db:"project_status"`
}

type ProjectDescriptionFilter struct {
	Ids               []uuid.UUID `json:"ids"`
	ProjectIds        []int       `json:"project_ids"`
	UserIds           []uuid.UUID `json:"user_ids"`
	ShortDescriptions []string    `json:"short_descriptions"`
	Descriptions      []string    `json:"descriptions"`
	ProjectStatuses   []Status    `json:"statuses"`
}
