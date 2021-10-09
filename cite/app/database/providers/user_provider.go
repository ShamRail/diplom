package providers

import (
	"cite_app/database"
	"cite_app/database/models"
	"cite_app/halpers"
	"fmt"
	"strings"
)

type IUserProvider interface {
	List(filter *models.UserFilter) []models.User
	Add(userList []models.User)
	Delete(userDelete *models.UserDelete)
	Update()
}

type UserProvider struct {
	DataBase *database.DataBase
}

func (u *UserProvider) List(filter *models.UserFilter) []models.User {
	var users []models.User
	var queryString = "SELECT * FROM users"
	if filter != nil {
		queryString += " WHERE ("
		var strs []string
		if filter.Ids != nil {
			strs = halpers.UuidsToStrings(filter.Ids)
			halpers.UseCommas(strs)
			queryString += fmt.Sprintf(" id in (%s) and", strings.Join(strs, ", "))
		}
		if filter.Names != nil {
			strs = filter.Names
			halpers.UseCommas(strs)
			queryString += fmt.Sprintf(" name in (%s) and", strings.Join(strs, ", "))
		}
		if filter.Passwords != nil {
			strs = filter.Passwords
			halpers.UseCommas(strs)
			queryString += fmt.Sprintf(" password in (%s) and", strings.Join(strs, ", "))
		}
		queryString = queryString[:len(queryString)-4] + ")"
	}
	u.DataBase.Db.Select(&users, queryString)
	return users
}

func (u *UserProvider) Add(userList []models.User) {
	u.DataBase.Db.NamedExec(`INSERT INTO users (id, name, password) VALUES (:id, :name, :password)`, userList)
}

func (u *UserProvider) Delete(userDelete *models.UserDelete) {
	var deleteString = "DELETE FROM users"
	if userDelete != nil {
		deleteString += " WHERE ("
		var strs []string
		if userDelete.Ids != nil {
			strs = halpers.UuidsToStrings(userDelete.Ids)
			halpers.UseCommas(strs)
			deleteString += fmt.Sprintf(" id in (%s) and", strings.Join(strs, ", "))
		}
		if userDelete.Names != nil {
			strs = userDelete.Names
			halpers.UseCommas(strs)
			deleteString += fmt.Sprintf(" name in (%s) and", strings.Join(strs, ", "))
		}
		if userDelete.Passwords != nil {
			strs = userDelete.Passwords
			halpers.UseCommas(strs)
			deleteString += fmt.Sprintf(" password in (%s) and", strings.Join(strs, ", "))
		}
		deleteString = deleteString[:len(deleteString)-4] + ")"
	}
	u.DataBase.Db.Exec(deleteString)
}

func (u *UserProvider) Update(user models.User) {
	var updateString = `UPDATE users SET name=$1, password=$2 WHERE id=$3`
	u.DataBase.Db.Exec(updateString, user.Name, user.Password, user.Id)
}
