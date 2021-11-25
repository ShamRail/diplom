package app_controllers

import (
	"encoding/json"
	uuid "github.com/satori/go.uuid"
	"net/http"
	. "site_app/database/models/project_doc_models"
)

func (app *App) AddProjectDoc(writer http.ResponseWriter, request *http.Request) {
	writer.Header().Set("Content-Type", "application/json")
	var projectDoc ProjectDoc
	var err = json.NewDecoder(request.Body).Decode(&projectDoc)
	if err != nil {
		writer.WriteHeader(500)
	} else {
		err = app.ProjectDocProvider.Add([]ProjectDoc{projectDoc})
		if err != nil {
			writer.WriteHeader(500)
		} else {
			writer.WriteHeader(200)
		}
	}
}

func (app *App) UpdateProjectDoc(writer http.ResponseWriter, request *http.Request) {
	writer.Header().Set("Content-Type", "application/json")
	var projectDoc ProjectDoc
	var err = json.NewDecoder(request.Body).Decode(&projectDoc)
	if err != nil {
		writer.WriteHeader(500)
	} else {
		err = app.ProjectDocProvider.Update(projectDoc)
		if err != nil {
			writer.WriteHeader(500)
		} else {
			writer.WriteHeader(200)
		}
	}
}

func (app *App) DeleteProjectDoc(writer http.ResponseWriter, request *http.Request) {
	writer.Header().Set("Content-Type", "application/json")
	var projectDocDelete ProjectDocDelete
	var err = json.NewDecoder(request.Body).Decode(&projectDocDelete)
	if err != nil {
		writer.WriteHeader(500)
	} else {
		err = app.ProjectDocProvider.Delete(&projectDocDelete)
		if err != nil {
			writer.WriteHeader(500)
		} else {
			writer.WriteHeader(200)
		}
	}
}

func (app *App) GetProjectDocs(writer http.ResponseWriter, request *http.Request) {
	writer.Header().Set("Content-Type", "application/json")
	projectIds, _ := request.URL.Query()["id"]
	var id = uuid.FromStringOrNil(projectIds[0])
	var projectDocFilter = &ProjectDocFilter{
		Ids: []uuid.UUID{
			id,
		},
		Names:            nil,
		ConfigurationIds: nil,
		BuildStatuses:    nil,
	}

	var ProjectDocs []ProjectDoc
	if projectDocFilter == nil {
		ProjectDocs = app.ProjectDocProvider.List(nil)
	} else {
		ProjectDocs = app.ProjectDocProvider.List(projectDocFilter)
	}

	json.NewEncoder(writer).Encode(ProjectDocs)
}

func (app *App) GetUserDocs(writer http.ResponseWriter, request *http.Request) {

	writer.Header().Set("Content-Type", "application/json")
	var projectDocFilter *ProjectDocFilter
	var err = json.NewDecoder(request.Body).Decode(&projectDocFilter)
	var ProjectDocs []ProjectDoc
	if err != nil {
		ProjectDocs = app.ProjectDocProvider.List(nil)
	} else {
		ProjectDocs = app.ProjectDocProvider.List(projectDocFilter)
	}

	json.NewEncoder(writer).Encode(ProjectDocs)

}
