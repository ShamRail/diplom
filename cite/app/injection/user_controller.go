package injection

import (
	"cite_app/database/models"
	"encoding/json"
	uuid "github.com/satori/go.uuid"
	"net/http"
)

func (app *Injection) AddUser(writer http.ResponseWriter, request *http.Request) {
	writer.Header().Set("Content-Type", "application/json")
	var user models.User
	var err = json.NewDecoder(request.Body).Decode(&user)
	if err != nil {
		http.Error(writer, http.StatusText(http.StatusInternalServerError), http.StatusInternalServerError)
	} else {
		err = app.UserProvider.Add([]models.User{
			user,
		})
		if err != nil {
			http.Error(writer, http.StatusText(http.StatusInternalServerError), http.StatusInternalServerError)
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
		http.Error(writer, http.StatusText(http.StatusInternalServerError), http.StatusInternalServerError)
	} else {
		err = app.UserProvider.Update(user)
		if err != nil {
			http.Error(writer, http.StatusText(http.StatusInternalServerError), http.StatusInternalServerError)
		} else {
			writer.WriteHeader(200)
		}
	}
}

func (app *Injection) DeleteUser(writer http.ResponseWriter, request *http.Request) {
	writer.Header().Set("Content-Type", "application/json")
	var userId uuid.UUID
	var err = json.NewDecoder(request.Body).Decode(&userId)
	if err != nil {
		http.Error(writer, http.StatusText(http.StatusInternalServerError), http.StatusInternalServerError)
	} else {
		err = app.UserProvider.Delete(&models.UserDelete{
			Ids: []uuid.UUID{userId},
		})
		if err != nil {
			http.Error(writer, http.StatusText(http.StatusInternalServerError), http.StatusInternalServerError)
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
