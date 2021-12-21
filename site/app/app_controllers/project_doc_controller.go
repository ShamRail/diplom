package app_controllers

import (
	"encoding/json"
	uuid "github.com/satori/go.uuid"
	"net/http"
	"site_app/database/models/intents"
	. "site_app/database/models/project_description"
	. "site_app/database/models/project_doc_models"
	. "site_app/database/models/user_project_models"
	"strconv"
)

func (app *App) AddProjectDoc(writer http.ResponseWriter, request *http.Request) {
	writer.Header().Set("Content-Type", "application/json")
	var projectDocIntent intents.ProjectDocIntent
	var err = json.NewDecoder(request.Body).Decode(&projectDocIntent)
	if err != nil {
		writer.WriteHeader(500)
		return
	}

	projectDoc := ProjectDoc{
		Name:            projectDocIntent.Name,
		SourceCodeUrl:   projectDocIntent.SourceCodeUrl,
		BuildCommand:    projectDocIntent.BuildCommand,
		RunCommand:      projectDocIntent.RunCommand,
		InFiles:         projectDocIntent.InFiles,
		OutFiles:        projectDocIntent.OutFiles,
		ConfigurationId: projectDocIntent.ConfigurationId,
		ArchiveInnerDir: projectDocIntent.ArchiveInnerDir,
	}

	id, err := app.ProjectDocProvider.Add(projectDoc)

	projectDocDescriptionId := uuid.NewV4()

	projectDocDescription := ProjectDescription{
		Id:               &projectDocDescriptionId,
		ProjectId:        id,
		UserId:           projectDocIntent.UserId,
		Author:           projectDocIntent.UserName,
		ShortDescription: projectDocIntent.Name,
		Description:      projectDocIntent.Description,
		ProjectStatus:    1,
	}

	app.ProjectDescriptionProvider.Add([]ProjectDescription{
		projectDocDescription,
	})
	uuidNew := uuid.NewV4()
	app.UserProjectProvider.Add([]UserProject{
		{
			Id:        &uuidNew,
			UserId:    projectDocIntent.UserId,
			ProjectId: id,
		},
	})
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

		descriptions, _ := app.ProjectDescriptionProvider.List(&ProjectDescriptionFilter{
			ProjectIds: projectDocDelete.Ids,
		})

		projectDescriptionsIds := make([]uuid.UUID, len(descriptions))
		for i := range descriptions {
			projectDescriptionsIds[i] = *descriptions[i].Id
		}

		app.ProjectDescriptionProvider.Delete(&ProjectDescriptionDelete{
			Ids: projectDescriptionsIds,
		})

		userProjects := app.UserProjectProvider.List(&UserProjectFilter{
			ProjectIds: projectDocDelete.Ids,
		})

		userProjectIds := make([]uuid.UUID, len(userProjects))
		for i := range userProjects {
			userProjectIds[i] = *userProjects[i].Id
		}

		app.UserProjectProvider.Delete(&UserProjectDelete{
			Ids: userProjectIds,
		})
	}
}

func (app *App) GetProjectDocs(writer http.ResponseWriter, request *http.Request) {
	writer.Header().Set("Content-Type", "application/json")
	projectIds, _ := request.URL.Query()["id"]
	var id, _ = strconv.Atoi(projectIds[0])
	var projectDocFilter = &ProjectDocFilter{
		Ids: []int{
			id,
		},
		Names:            nil,
		ConfigurationIds: nil,
		BuildStatuses:    nil,
	}

	ProjectDocs := app.ProjectDocProvider.List(projectDocFilter)

	json.NewEncoder(writer).Encode(ProjectDocs)
}

func (app *App) GetProjectDocsByUserId(writer http.ResponseWriter, request *http.Request) {
	writer.Header().Set("Content-Type", "application/json")
	userId, _ := request.URL.Query()["userId"]
	var id = uuid.FromStringOrNil(userId[0])

	var userProjectDoc = app.UserProjectProvider.List(&UserProjectFilter{UserIds: []uuid.UUID{
		id,
	}})

	if userProjectDoc != nil {
		projectIds := make([]int, len(userProjectDoc))
		for i := range userProjectDoc {
			projectIds[i] = userProjectDoc[i].ProjectId
		}

		var projectDocFilter = &ProjectDocFilter{
			Ids:              projectIds,
			Names:            nil,
			ConfigurationIds: nil,
			BuildStatuses:    nil,
		}

		ProjectDocs := app.ProjectDocProvider.List(projectDocFilter)

		json.NewEncoder(writer).Encode(ProjectDocs)
		return
	}
	json.NewEncoder(writer).Encode([]ProjectDoc{})
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
