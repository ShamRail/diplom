package main

import (
	"github.com/gorilla/mux"
	"net/http"
	"site_app/app_controllers"
)

var (
	a = http.FileServer(http.Dir("resources/static"))
)

func addRoutes(app *app_controllers.App) *mux.Router {
	router := mux.NewRouter()
	/*user controllers*/
	router.HandleFunc("/app/users", app.AddUser).Methods("POST")
	router.HandleFunc("/app/users", app.Auth.BasicAuth(app.GetUsers)).Methods("GET")
	router.HandleFunc("/app/delete_user", app.Auth.BasicAuth(app.DeleteUser)).Methods("DELETE")
	router.HandleFunc("/app/user", app.Auth.BasicAuth(app.UpdateUser)).Methods("PUT")

	/*project_doc controllers*/
	router.HandleFunc("/app/project_doc", app.Auth.BasicAuth(app.AddProjectDoc)).Methods("POST")
	router.HandleFunc("/app/project_docs", app.GetProjectDocs).Methods("GET")
	router.HandleFunc("/app/delete_project_doc", app.Auth.BasicAuth(app.DeleteProjectDoc)).Methods("DELETE")
	router.HandleFunc("/app/project_doc", app.Auth.BasicAuth(app.UpdateProjectDoc)).Methods("PUT")

	/*project_description*/
	router.HandleFunc("/app/project_description/filter", app.GetProjectDescriptionFilter).Methods("GET")
	router.HandleFunc("/app/project_description/all", app.GetAllProjectDescription).Methods("GET")

	/*user_project_doc*/
	router.HandleFunc("/app/user_doc", app.GetProject).Methods("POST")
	router.HandleFunc("/app/project_doc/user", app.Auth.BasicAuth(app.GetProjectDocsByUserId)).Methods("GET")

	/*service controller*/
	router.HandleFunc("/app/configuration/all", app.GetAllConfigurations).Methods("GET")
	router.HandleFunc("/app/status", app.GetProjectStatus).Methods("GET")
	router.HandleFunc("/app/build", app.BuildProject).Methods("POST")

	/*set file directory*/
	router.PathPrefix("/app/").Handler(http.StripPrefix("/app/", a))

	return router
}
