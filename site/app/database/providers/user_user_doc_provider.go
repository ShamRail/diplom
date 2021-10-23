package providers

import (
	"fmt"
	"site_app/database"
	m "site_app/database/models/user_user_doc_models"
	"site_app/helpers"
	"strings"
)

type IUserDocProvider interface {
	List(filter *m.UserProjectDocFilter) []m.UserUserDoc
	Delete(toDelete *m.UserProjectDocDelete) error
	Update(toUpdate m.UserUserDoc) error
	Add(toAdd []m.UserUserDoc) error
}

type UserDocProvider struct {
	DataBase *database.DataBase
}

func (u *UserDocProvider) List(filter *m.UserProjectDocFilter) []m.UserUserDoc {
	var uDoc []m.UserUserDoc
	var queryString = "SELECT * FROM user_project_doc"
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
			strs = helpers.UuidsToStrings(filter.ProjectIds)
			helpers.UseCommas(strs)
			queryString += fmt.Sprintf(" project_id in (%s) and", strings.Join(strs, ", "))
		}
		queryString = queryString[:len(queryString)-4] + ")"
	}
	u.DataBase.Db.Select(&uDoc, queryString)
	return uDoc
}

func (u *UserDocProvider) Delete(toDelete *m.UserProjectDocDelete) error {
	var deleteString = "DELETE FROM user_project_doc"
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

func (u *UserDocProvider) Update(toUpdate m.UserUserDoc) error {
	var updateString = `UPDATE user_project_doc SET user_id=$1, project_id=$2 WHERE id=$3`
	var _, err = u.DataBase.Db.Exec(updateString, toUpdate.UserId, toUpdate.ProjectId, toUpdate.Id)
	return err
}

func (u *UserDocProvider) Add(toAdd []m.UserUserDoc) error {
	var _, err = u.DataBase.Db.NamedExec(
		`INSERT INTO user_project_doc (id,
                   user_id,
                   project_id) 
                   VALUES (:id,
                           :user_id,
                           :project_id)`,
		toAdd)
	return err
}
