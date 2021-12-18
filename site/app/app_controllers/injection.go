package app_controllers

import (
	"site_app/database/providers"
	"site_app/services"
)

type App struct {
	UserProvider               providers.IUserProvider
	ProjectDocProvider         providers.IProjectDocProvider
	ProjectDescriptionProvider providers.IProjectDescriptionProvider
	UserProjectProvider        providers.IUserProjectProvider
	Auth                       services.IAuthService
}
