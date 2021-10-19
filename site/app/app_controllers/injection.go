package app_controllers

import (
	"site_app/database/providers"
	"site_app/services"
)

type App struct {
	UserProvider       providers.IUserProvider
	ProjectDocProvider providers.IProjectDocProvider
	Auth               services.IAuthService
}
