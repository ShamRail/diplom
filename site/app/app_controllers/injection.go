package app_controllers

import (
	"site_app/database/providers"
)

type App struct {
	UserProvider       *providers.UserProvider
	ProjectDocProvider *providers.ProjectDocProvider
}
