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
	router.HandleFunc("/delete_user", app.Auth.BasicAuth(app.DeleteUser)).Methods("POST")
	router.HandleFunc("/user", app.Auth.BasicAuth(app.UpdateUser)).Methods("PUT")

	/*project_doc controllers*/
	router.HandleFunc("/project_doc", app.Auth.BasicAuth(app.AddProjectDoc)).Methods("POST")
	router.HandleFunc("/project_docs", app.Auth.BasicAuth(app.GetProjectDocs)).Methods("GET")
	router.HandleFunc("/delete_project_doc", app.Auth.BasicAuth(app.DeleteProjectDoc)).Methods("POST")
	router.HandleFunc("/project_docs", app.Auth.BasicAuth(app.UpdateProjectDoc)).Methods("PUT")

	/*set file directory*/
	router.PathPrefix("/app/").Handler(http.StripPrefix("/app/", a))

	return router
}
