package app_controllers

import (
	"encoding/json"
	uuid "github.com/satori/go.uuid"
	"net/http"
	"site_app/database/models/project_description"
)

func (app *App) GetProjectDescriptionFilter(writer http.ResponseWriter, request *http.Request) {
	writer.Header().Set("Content-Type", "application/json")
	query := request.URL.Query()
	var projectId = uuid.FromStringOrNil(query.Get("project_id"))

	var projectDocFilter = &project_description.ProjectDescriptionFilter{
		ProjectIds: []uuid.UUID{
			projectId,
		},
	}

	ProjectDescriptions, err := app.ProjectDescriptionProvider.List(projectDocFilter)

	if err != nil {
		json.NewEncoder(writer).Encode(err)
	}
	json.NewEncoder(writer).Encode(ProjectDescriptions)
}

func (app *App) GetAllProjectDescription(writer http.ResponseWriter, request *http.Request) {
	writer.Header().Set("Content-Type", "application/json")
	ProjectDescriptions, err := app.ProjectDescriptionProvider.List(nil)
	if err != nil {
		json.NewEncoder(writer).Encode(err)
	}
	json.NewEncoder(writer).Encode(ProjectDescriptions)
}
