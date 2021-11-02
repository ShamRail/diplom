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
	ProjectId        *uuid.UUID `db:"project_id"`
	ShortDescription string     `db:"short_description"`
	Description      string     `db:"description"`
	ProjectStatus    Status     `db:"status"`
}

type ProjectDescriptionFilter struct {
	Ids               []uuid.UUID `json:"ids"`
	ProjectIds        []uuid.UUID `json:"project_ids"`
	ShortDescriptions []string    `json:"short_descriptions"`
	Descriptions      []string    `json:"descriptions"`
	ProjectStatuses   []Status    `json:"statuses"`
}
