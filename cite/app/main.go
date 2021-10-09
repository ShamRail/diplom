package main

import (
	"cite_app/database"
	"cite_app/database/providers"
	"cite_app/injection"
	"github.com/gorilla/handlers"
	"log"
	"net/http"
	"os"
)

func main() {

	var user = os.Getenv("POSTGRES_USER")
	var password = os.Getenv("POSTGRES_PASSWORD")
	var dbName = os.Getenv("POSTGRES_DB")
	var host = "cite_db"
	var port = "5432"
	if user == "" || password == "" {
		host = "localhost"
		user = "postgres"
		password = "postgres"
		dbName = "postgres"
	}
	var db = database.CreateDataBase(host, port, user, password, dbName, "disable")
	log.Printf("Database connected with: \n"+
		"host: %s \n"+
		"port: %s \n"+
		"user: %s \n"+
		"password: %s \n"+
		"dbName: %s \n",
		host,
		port,
		user,
		password,
		dbName)

	var us = providers.UserProvider{
		DataBase: db,
	}
	var app = injection.Injection{
		UserProvider: &us,
	}

	var router = addRoutes(&app)

	log.Fatal(http.ListenAndServe(":80",
		handlers.CORS(
			handlers.AllowedHeaders([]string{"X-Requested-With", "Content-Type", "Authorization"}),
			handlers.AllowedMethods([]string{"GET", "POST", "PUT", "HEAD", "OPTIONS"}),
			handlers.AllowedOrigins([]string{"*"}))(router)))
}
