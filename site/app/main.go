package main

import (
	"log"
	"net/http"
	"os"
	"site_app/app_controllers"
	"site_app/database"
	"site_app/database/providers"
)

func main() {

	var user = os.Getenv("POSTGRES_USER")
	var password = os.Getenv("POSTGRES_PASSWORD")
	var dbName = os.Getenv("POSTGRES_DB")
	var host = os.Getenv("POSTGRES_HOST")
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
	var app = app_controllers.App{
		UserProvider: &us,
	}

	var router = addRoutes(&app)

	log.Fatal(http.ListenAndServe(":8080", router))
}
