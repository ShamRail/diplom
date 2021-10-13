package main

import (
	"github.com/gorilla/mux"
	"net/http"
	"site_app/app_controllers"
)

func addRoutes(app *app_controllers.App) *mux.Router {
	router := mux.NewRouter()

	router.HandleFunc("/users", app.AddUser).Methods("POST")
	router.HandleFunc("/users", app.GetUsers).Methods("GET")
	router.HandleFunc("/delete_user", app.DeleteUser).Methods("POST")
	router.HandleFunc("/user", app.UpdateUser).Methods("PUT")

	var a = http.FileServer(http.Dir("resources/static"))
	router.PathPrefix("/app/").Handler(http.StripPrefix("/app/", a))

	return router
}
