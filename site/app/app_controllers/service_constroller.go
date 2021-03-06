package app_controllers

import (
	"net/http"
	"site_app/database/models/project_doc_models"
	"strconv"
)

func (app *App) GetAllConfigurations(writer http.ResponseWriter, request *http.Request) {
	res, err := app.BuilderService.GetConfigurationAll()
	if err == nil {
		writer.Write([]byte(res))
	} else {
		writer.WriteHeader(500)
	}
}

func (app *App) GetProjectStatus(writer http.ResponseWriter, request *http.Request) {
	ids, _ := request.URL.Query()["id"]
	id := ids[0]
	res, err := app.BuilderService.GetBuildStatus(id)
	if err == nil {
		writer.Write([]byte(res))
	} else {
		writer.WriteHeader(500)
	}
}

func (app *App) BuildProject(writer http.ResponseWriter, request *http.Request) {
	writer.Header().Set("Content-Type", "application/json")
	id, _ := strconv.Atoi(request.FormValue("id"))
	projectDocs := app.ProjectDocProvider.List(&project_doc_models.ProjectDocFilter{Ids: []int{id}})
	resp, err := app.BuilderService.Build(projectDocs[0])
	if err == nil {
		writer.Write([]byte(resp))
	} else {
		writer.WriteHeader(500)
	}
}
