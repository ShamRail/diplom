package providers

import (
	"fmt"
	"site_app/database"
	"site_app/database/models/user_models"
	"site_app/halpers"
	"strings"
)

type IUserProvider interface {
	List(filter *user_models.UserFilter) []user_models.User
	Add(userList []user_models.User) error
	Delete(userDelete *user_models.UserDelete) error
	Update(user user_models.User) error
}

type UserProvider struct {
	DataBase *database.DataBase
}

func (u *UserProvider) List(filter *user_models.UserFilter) []user_models.User {
	var users []user_models.User
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

func (u *UserProvider) Add(userList []user_models.User) error {
	var _, err = u.DataBase.Db.NamedExec(
		`INSERT INTO users (id,
                   name,
                   password) 
                   VALUES (:id,
                           :name,
                           :password)`,
		userList)
	return err
}

func (u *UserProvider) Delete(userDelete *user_models.UserDelete) error {
	var deleteString = "DELETE FROM users"
	if userDelete != nil {
		deleteString += " WHERE ("
		var strs []string
		if userDelete.Ids != nil {
			strs = halpers.UuidsToStrings(userDelete.Ids)
			halpers.UseCommas(strs)
			deleteString += fmt.Sprintf(" id in (%s) and", strings.Join(strs, ", "))
		}
		deleteString = deleteString[:len(deleteString)-4] + ")"
	}
	var _, err = u.DataBase.Db.Exec(deleteString)
	return err
}

func (u *UserProvider) Update(user user_models.User) error {
	var updateString = `UPDATE users SET name=$1, password=$2 WHERE id=$3`
	var _, err = u.DataBase.Db.Exec(updateString, user.Name, user.Password, user.Id)
	return err
}
