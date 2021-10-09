package main

import (
	"cite_app/injection"
	"github.com/gorilla/mux"
)

func addRoutes(app *injection.Injection) *mux.Router {
	router := mux.NewRouter()

	router.HandleFunc("/users", app.AddUser).Methods("POST")
	router.HandleFunc("/users", app.GetUsers).Methods("GET")
	router.HandleFunc("/delete_user", app.DeleteUser).Methods("POST")
	router.HandleFunc("/user", app.UpdateUser).Methods("PUT")

	return router
}
