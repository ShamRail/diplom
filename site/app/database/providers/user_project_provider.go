package providers

import (
	"fmt"
	"site_app/database"
	m "site_app/database/models/user_project_models"
	"site_app/helpers"
	"strings"
)

type IUserProjectProvider interface {
	List(filter *m.UserProjectFilter) []m.UserProject
	Delete(toDelete *m.UserProjectDelete) error
	Update(toUpdate m.UserProject) error
	Add(toAdd []m.UserProject) error
}

type UserProjectProvider struct {
	DataBase *database.DataBase
}

func (u *UserProjectProvider) List(filter *m.UserProjectFilter) []m.UserProject {
	var uDoc []m.UserProject
	var queryString = "SELECT * FROM user_project"
	if filter != nil {
		queryString += " WHERE ("
		var strs []string
		if filter.Ids != nil {
			strs = helpers.UuidsToStrings(filter.Ids)
			helpers.UseCommas(strs)
			queryString += fmt.Sprintf(" id in (%s) and", strings.Join(strs, ", "))
		}
		if filter.UserIds != nil {
			strs = helpers.UuidsToStrings(filter.UserIds)
			helpers.UseCommas(strs)
			queryString += fmt.Sprintf(" user_id in (%s) and", strings.Join(strs, ", "))
		}
		if filter.ProjectIds != nil {
			strs = helpers.UseCommasIntsToString(filter.ProjectIds)
			helpers.UseCommas(strs)
			queryString += fmt.Sprintf(" project_id in (%s) and", strings.Join(strs, ", "))
		}
		queryString = queryString[:len(queryString)-4] + ")"
	}
	u.DataBase.Db.Select(&uDoc, queryString)
	return uDoc
}

func (u *UserProjectProvider) Delete(toDelete *m.UserProjectDelete) error {
	var deleteString = "DELETE FROM user_project"
	if toDelete != nil {
		deleteString += " WHERE ("
		var strs []string
		if toDelete.Ids != nil {
			strs = helpers.UuidsToStrings(toDelete.Ids)
			helpers.UseCommas(strs)
			deleteString += fmt.Sprintf(" id in (%s) and", strings.Join(strs, ", "))
		}
		deleteString = deleteString[:len(deleteString)-4] + ")"
	}
	var _, err = u.DataBase.Db.Exec(deleteString)
	return err
}

func (u *UserProjectProvider) Update(toUpdate m.UserProject) error {
	var updateString = `UPDATE user_project SET user_id=$1, project_id=$2 WHERE id=$3`
	var _, err = u.DataBase.Db.Exec(updateString, toUpdate.UserId, toUpdate.ProjectId, toUpdate.Id)
	return err
}

func (u *UserProjectProvider) Add(toAdd []m.UserProject) error {
	var _, err = u.DataBase.Db.NamedExec(
		`INSERT INTO user_project (id,
                   user_id,
                   project_id) 
                   VALUES (:id,
                           :user_id,
                           :project_id)`,
		toAdd)
	return err
}
