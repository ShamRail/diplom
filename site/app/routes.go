package main

import (
	"github.com/gorilla/mux"
	"net/http"
	"site_app/app_controllers"
)

var (
	a      = http.FileServer(http.Dir("resources/static"))
	router = mux.NewRouter()
)

func addRoutes(app *app_controllers.App) *mux.Router {

	/*user controllers*/
	router.HandleFunc("/users", app.AddUser).Methods("POST")
	router.HandleFunc("/users", app.Auth.BasicAuth(app.GetUsers)).Methods("GET")
	router.HandleFunc("/delete_user", app.Auth.BasicAuth(app.DeleteUser)).Methods("DELETE")
	router.HandleFunc("/user", app.Auth.BasicAuth(app.UpdateUser)).Methods("PUT")

	/*project_doc controllers*/
	router.HandleFunc("/project_doc", app.Auth.BasicAuth(app.AddProjectDoc)).Methods("POST")
	router.HandleFunc("/project_docs", app.GetProjectDocs).Methods("GET")
	router.HandleFunc("/delete_project_doc", app.Auth.BasicAuth(app.DeleteProjectDoc)).Methods("DELETE")
	router.HandleFunc("/project_docs", app.Auth.BasicAuth(app.UpdateProjectDoc)).Methods("PUT")

	/*project_description*/
	router.HandleFunc("/project_description", app.GetProjectDescription).Methods("GET")

	/*user_project_doc*/
	router.HandleFunc("/user_doc", app.Auth.BasicAuth(app.GetProject)).Methods("POST")

	/*set file directory*/
	router.PathPrefix("/app/").Handler(http.StripPrefix("/app/", a))

	return router
}
