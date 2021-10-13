package providers

import (
	"fmt"
	"site_app/database"
	"site_app/database/models/project_doc_models"
	"site_app/halpers"
	"strings"
)

type IProjectDocProvider interface {
	List(filter *project_doc_models.ProjectDocFilter) []project_doc_models.ProjectDoc
	Add(projectDocList []project_doc_models.ProjectDoc) error
	Delete(projectDocDelete *project_doc_models.ProjectDocDelete) error
	Update(projectDoc project_doc_models.ProjectDoc) error
}

type ProjectDocProvider struct {
	DataBase *database.DataBase
}

func (pd *ProjectDocProvider) List(filter *project_doc_models.ProjectDocFilter) []project_doc_models.ProjectDoc {
	var projectDocs []project_doc_models.ProjectDoc
	var queryString = "SELECT * FROM project_docs"
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
		if filter.BuildStatuses != nil {
			strs = filter.BuildStatuses
			halpers.UseCommas(strs)
			queryString += fmt.Sprintf(" build_status in (%s) and", strings.Join(strs, ", "))
		}
		if filter.ConfigurationIds != nil {
			var confIds = filter.ConfigurationIds
			strs = halpers.UseCommasToString(confIds)
			queryString += fmt.Sprintf(" configuration_id in (%s) and", strings.Join(strs, ", "))
		}
		queryString = queryString[:len(queryString)-4] + ")"
	}
	pd.DataBase.Db.Select(&projectDocs, queryString)
	return projectDocs
}

func (pd *ProjectDocProvider) Add(doc []project_doc_models.ProjectDoc) error {
	var _, err = pd.DataBase.Db.NamedExec(
		`INSERT INTO project_docs (
                          id,
                          name,
                          source_code_url,
                          build_command,
                          run_command,
                          in_files,
                          out_files,
                          configuration_id,
                          build_status,
                          archive_inner_dir)
                          VALUES (
                                  :id,
                                  :name,
                                  :source_code_url,
                                  :build_command,
                                  :run_command,
                                  :in_files,
                                  :out_files,
                                  :configuration_id,
                                  :build_status,
                                  :archive_inner_dir)`,
		doc)
	return err
}

func (pd *ProjectDocProvider) Delete(doc *project_doc_models.ProjectDocDelete) error {
	var deleteString = "DELETE FROM project_docs"
	if doc != nil {
		deleteString += " WHERE ("
		var strs []string
		if doc.Ids != nil {
			strs = halpers.UuidsToStrings(doc.Ids)
			halpers.UseCommas(strs)
			deleteString += fmt.Sprintf(" id in (%s) and", strings.Join(strs, ", "))
		}
		deleteString = deleteString[:len(deleteString)-4] + ")"
	}
	var _, err = pd.DataBase.Db.Exec(deleteString)
	return err
}

func (pd *ProjectDocProvider) Update(doc project_doc_models.ProjectDoc) error {
	var updateString = `UPDATE project_docs SET name = $1,
                          source_code_url = $2,
                          build_command = $3,
                          run_command = $4,
                          in_files = $5,
                          out_files = $6,
                          configuration_id = $7,
                          build_status = $8,
                          archive_inner_dir = $9
							WHERE id=$10`

	var _, err = pd.DataBase.Db.Exec(
		updateString,
		doc.Name,
		doc.SourceCodeUrl,
		doc.BuildCommand,
		doc.RunCommand,
		doc.InFiles,
		doc.OutFiles,
		doc.ConfigurationId,
		doc.BuildStatus,
		doc.ArchiveInnerDir,
		doc.Id)

	return err
}
