package services

import (
	"crypto/sha256"
	"crypto/subtle"
	"net/http"
	"site_app/database/models/user_models"
	"site_app/database/providers"
)

type IAuthService interface {
	BasicAuth(next http.HandlerFunc) http.HandlerFunc
	isAuth(username, password string) bool
}

type AuthService struct {
	userProvider providers.IUserProvider
}

func (auth *AuthService) isAuth(username, password string) bool {
	usernameHash := sha256.Sum256([]byte(username))
	passwordHash := sha256.Sum256([]byte(password))

	var user = auth.userProvider.List(&user_models.UserFilter{Names: []string{
		username,
	}})[0]

	expectedUsernameHash := sha256.Sum256([]byte(user.Name))
	expectedPasswordHash := sha256.Sum256([]byte(user.Password))

	return subtle.ConstantTimeCompare(usernameHash[:], expectedUsernameHash[:]) == 1 &&
		subtle.ConstantTimeCompare(passwordHash[:], expectedPasswordHash[:]) == 1
}

func (auth *AuthService) BasicAuth(next http.HandlerFunc) http.HandlerFunc {

	return func(w http.ResponseWriter, r *http.Request) {
		username, password, ok := r.BasicAuth()
		if ok {
			var isOk = auth.isAuth(username, password)
			if isOk {
				next.ServeHTTP(w, r)
				return
			}
		}

		w.Header().Set("WWW-Authenticate", `Basic realm="restricted", charset="UTF-8"`)
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
	}
}

func CreateNewAuthService(userProvider providers.IUserProvider) IAuthService {
	return &AuthService{userProvider: userProvider}
}
