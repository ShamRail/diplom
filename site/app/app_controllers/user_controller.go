package app_controllers

import (
	"encoding/json"
	"net/http"
	"site_app/database/models/project_doc_models"
	. "site_app/database/models/user_models"
)

func (app *App) AddUser(writer http.ResponseWriter, request *http.Request) {

	writer.Header().Set("Content-Type", "application/json")
	var user User
	var err = json.NewDecoder(request.Body).Decode(&user)
	if err != nil {
		writer.WriteHeader(500)
	} else {
		if app.Auth.CheckEmail(user.Email) {
			var exist, _ = app.UserProvider.List(&UserFilter{
				Emails: []string{
					user.Email,
				},
			})
			if exist == nil {
				err = app.UserProvider.Add([]User{
					user,
				})
				if err != nil {
					writer.WriteHeader(500)
					writer.Write([]byte("Can not add user"))
				} else {
					writer.WriteHeader(200)
				}
			} else {
				writer.WriteHeader(500)
				writer.Write([]byte("User with the same email already exist"))
			}
		} else {
			writer.WriteHeader(500)
			writer.Write([]byte("This email does not exist"))
		}
	}
}

func (app *App) UpdateUser(writer http.ResponseWriter, request *http.Request) {
	writer.Header().Set("Content-Type", "application/json")
	var user User
	var err = json.NewDecoder(request.Body).Decode(&user)
	if err != nil {
		writer.WriteHeader(500)
	} else {
		err = app.UserProvider.Update(user)
		if err != nil {
			writer.WriteHeader(500)
		} else {
			writer.WriteHeader(200)
		}
	}
}

func (app *App) DeleteUser(writer http.ResponseWriter, request *http.Request) {
	writer.Header().Set("Content-Type", "application/json")
	var userDelete UserDelete
	var err = json.NewDecoder(request.Body).Decode(&userDelete)
	if err != nil {
		writer.WriteHeader(500)
	} else {
		err = app.UserProvider.Delete(&userDelete)
		if err != nil {
			writer.WriteHeader(500)
		} else {
			writer.WriteHeader(200)
		}
	}
}

func (app *App) GetUsers(writer http.ResponseWriter, request *http.Request) {
	writer.Header().Set("Content-Type", "application/json")

	query := request.URL.Query()
	email := query.Get("email")

	users, _ := app.UserProvider.List(&UserFilter{Emails: []string{email}})

	json.NewEncoder(writer).Encode(users)
}

func (app *App) GetProject(writer http.ResponseWriter, request *http.Request) {
	writer.Header().Set("Content-Type", "application/json")
	var projectDocFilter *project_doc_models.ProjectDocFilter
	var err = json.NewDecoder(request.Body).Decode(&projectDocFilter)
	var projectDoc []project_doc_models.ProjectDoc
	if err != nil {
		projectDoc = app.ProjectDocProvider.List(nil)
	} else {
		projectDoc = app.ProjectDocProvider.List(projectDocFilter)
	}

	json.NewEncoder(writer).Encode(projectDoc)
}
