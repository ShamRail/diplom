package app_controllers

import (
	"encoding/json"
	uuid "github.com/satori/go.uuid"
	"net/http"
	"site_app/database/models/project_description"
)

func (app *App) GetProjectDescription(writer http.ResponseWriter, request *http.Request) {
	writer.Header().Set("Content-Type", "application/json")
	query := request.URL.Query()
	var id = uuid.FromStringOrNil(query.Get("id"))
	var projectDocFilter = &project_description.ProjectDescriptionFilter{
		Ids: []uuid.UUID{
			id,
		},
	}

	var ProjectDescriptions []project_description.ProjectDescription

	var err error

	if projectDocFilter == nil {
		ProjectDescriptions, err = app.ProjectDescriptionProvider.List(nil)
	} else {
		ProjectDescriptions, err = app.ProjectDescriptionProvider.List(projectDocFilter)
	}
	if err != nil {
		json.NewEncoder(writer).Encode(err)
	}
	json.NewEncoder(writer).Encode(ProjectDescriptions)
}
