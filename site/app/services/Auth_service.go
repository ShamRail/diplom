package services

import (
	"crypto/sha256"
	"fmt"
	"github.com/AfterShip/email-verifier"
	"github.com/smancke/mailck"
	"net/http"
	"site_app/database/models/user_models"
	"site_app/database/providers"
)

type IAuthService interface {
	BasicAuth(next http.HandlerFunc) http.HandlerFunc
	isAuth(username, password string) bool
	CheckEmail(email string) bool
}

type AuthService struct {
	userProvider providers.IUserProvider
}

var (
	verifier = emailverifier.
		NewVerifier().
		EnableSMTPCheck()
)

func (auth *AuthService) isAuth(username, password string) bool {
	passwordHash := sha256.Sum256([]byte(password))

	var users, _ = auth.userProvider.List(&user_models.UserFilter{Emails: []string{
		username,
	}})

	if len(users) == 0 {
		return false
	}
	var user = users[0]
	expectedUsername := user.Email

	expectedPasswordHash := string(user.Password)
	var s = fmt.Sprintf("%x", passwordHash)

	var res = expectedUsername == username && expectedPasswordHash == s
	return res
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

func (auth *AuthService) CheckEmail(email string) bool {

	result, _ := mailck.Check("noreply@mancke.net", email)
	switch {

	case result.IsValid():
		return true

	case result.IsError():
		return false

	case result.IsInvalid():
		return false
	default:
		return false
	}
}

func CreateNewAuthService(userProvider providers.IUserProvider) IAuthService {
	return &AuthService{userProvider: userProvider}
}
