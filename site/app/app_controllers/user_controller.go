package app_controllers

import (
	"encoding/json"
	"net/http"
	. "site_app/database/models/user_models"
)

func (app *App) AddUser(writer http.ResponseWriter, request *http.Request) {

	writer.Header().Set("Content-Type", "application/json")
	var user User
	var err = json.NewDecoder(request.Body).Decode(&user)
	if err != nil {
		writer.WriteHeader(500)
	} else {
		err = app.UserProvider.Add([]User{
			user,
		})
		if err != nil {
			writer.WriteHeader(500)
		} else {
			writer.WriteHeader(200)
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
	var userFilter *UserFilter
	var err = json.NewDecoder(request.Body).Decode(&userFilter)
	var users []User
	if err != nil {
		users = app.UserProvider.List(nil)
	} else {
		users = app.UserProvider.List(userFilter)
	}

	json.NewEncoder(writer).Encode(users)
}
