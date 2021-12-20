package app_controllers

import (
	"log"
	"net/http"
)

func (app *App) GetAllConfigurations(writer http.ResponseWriter, request *http.Request) {
	res, err := app.BuilderService.GetConfigurationAll()
	log.Println("Trying get configurations")
	if err == nil {
		writer.Write([]byte(res))
	} else {
		writer.Write([]byte(err.Error()))
	}
}
