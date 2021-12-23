package providers

import (
	"fmt"
	"site_app/database"
	p "site_app/database/models/project_description"
	"site_app/helpers"
	"strings"
)

type IProjectDescriptionProvider interface {
	List(filter *p.ProjectDescriptionFilter) ([]p.ProjectDescription, error)
	Add(projectDocList []p.ProjectDescription) error
	Delete(projectDocDelete *p.ProjectDescriptionDelete) error
	Update(projectDoc p.ProjectDescription) error
}

type ProjectDescriptionProvider struct {
	DataBase *database.DataBase
}

func (pd *ProjectDescriptionProvider) List(filter *p.ProjectDescriptionFilter) ([]p.ProjectDescription, error) {
	var projectDocs []p.ProjectDescription
	var queryString = "SELECT * FROM project_description"
	if filter != nil {
		queryString += " WHERE ("
		var strs []string
		if filter.Ids != nil {
			strs = helpers.UuidsToStrings(filter.Ids)
			helpers.UseCommas(strs)
			queryString += fmt.Sprintf(" id in (%s) and", strings.Join(strs, ", "))
		}
		if filter.ProjectStatuses != nil {
			helpers.UseCommasStatusesToString(filter.ProjectStatuses)
			queryString += fmt.Sprintf(" project_status in (%s) and", strings.Join(strs, ", "))
		}
		if filter.ShortDescriptions != nil {
			strs = filter.ShortDescriptions
			helpers.UseCommas(strs)
			queryString += fmt.Sprintf(" short_description in (%s) and", strings.Join(strs, ", "))
		}
		if filter.Descriptions != nil {
			strs = filter.Descriptions
			helpers.UseCommas(strs)
			queryString += fmt.Sprintf(" description in (%s) and", strings.Join(strs, ", "))
		}
		if filter.ProjectIds != nil {
			strs = helpers.UseCommasIntsToString(filter.ProjectIds)
			helpers.UseCommas(strs)
			queryString += fmt.Sprintf(" project_id in (%s) and", strings.Join(strs, ", "))
		}
		if filter.UserIds != nil {
			strs = helpers.UuidsToStrings(filter.UserIds)
			helpers.UseCommas(strs)
			queryString += fmt.Sprintf(" user_id in (%s) and", strings.Join(strs, ", "))
		}
		queryString = queryString[:len(queryString)-4] + ")"
	}
	var err = pd.DataBase.Db.Select(&projectDocs, queryString)
	return projectDocs, err
}

func (pd *ProjectDescriptionProvider) Add(d []p.ProjectDescription) error {
	var _, err = pd.DataBase.Db.NamedExec(
		`INSERT INTO project_description (id, project_id, author, user_id, short_description, description, project_status)
                          VALUES (
                                  :id,
                                  :project_id,
                                  :author,
                                  :user_id,
                                  :short_description,
                                  :description,
                                  :project_status)`, d)

	return err
}

func (pd *ProjectDescriptionProvider) Delete(doc *p.ProjectDescriptionDelete) error {
	var deleteString = "DELETE FROM project_description"
	if doc != nil {
		deleteString += " WHERE ("
		var strs []string
		if doc.Ids != nil {
			strs = helpers.UuidsToStrings(doc.Ids)
			helpers.UseCommas(strs)
			deleteString += fmt.Sprintf(" id in (%s) and", strings.Join(strs, ", "))
		}
		deleteString = deleteString[:len(deleteString)-4] + ")"
	}
	var _, err = pd.DataBase.Db.Exec(deleteString)
	return err
}

func (pd *ProjectDescriptionProvider) Update(p p.ProjectDescription) error {

	updateString := `UPDATE project_description SET description = $1,
                          project_id = $2,
                          short_description = $3
						  WHERE id = $4`

	var _, err = pd.DataBase.Db.Exec(
		updateString,
		p.Description,
		p.ProjectId,
		p.ShortDescription,
		p.Id)

	return err
}
