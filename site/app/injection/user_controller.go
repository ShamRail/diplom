package injection

import (
	"encoding/json"
	"net/http"
	"site_app/database/models"
)

func (app *Injection) AddUser(writer http.ResponseWriter, request *http.Request) {
	writer.Header().Set("Content-Type", "application/json")
	var user models.User
	var err = json.NewDecoder(request.Body).Decode(&user)
	if err != nil {
		writer.WriteHeader(500)
	} else {
		err = app.UserProvider.Add([]models.User{
			user,
		})
		if err != nil {
			writer.WriteHeader(500)
		} else {
			writer.WriteHeader(200)
		}
	}
}

func (app *Injection) UpdateUser(writer http.ResponseWriter, request *http.Request) {
	writer.Header().Set("Content-Type", "application/json")
	var user models.User
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

func (app *Injection) DeleteUser(writer http.ResponseWriter, request *http.Request) {
	writer.Header().Set("Content-Type", "application/json")
	var userDelete models.UserDelete
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

func (app *Injection) GetUsers(writer http.ResponseWriter, request *http.Request) {
	writer.Header().Set("Content-Type", "application/json")
	var userFilter *models.UserFilter
	var err = json.NewDecoder(request.Body).Decode(&userFilter)
	var users []models.User
	if err != nil {
		users = app.UserProvider.List(nil)
	} else {
		users = app.UserProvider.List(userFilter)
	}

	json.NewEncoder(writer).Encode(users)
}
