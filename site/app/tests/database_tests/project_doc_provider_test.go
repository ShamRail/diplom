package database_tests

/*package database_tests

import (
	"log"
	"site_app/database/models/project_doc_models"
	"site_app/database/providers"
	"testing"
)

var projectDocProvider = providers.ProjectDocProvider{DataBase: db}

var projectDocIds = []int{
	3,
	4,
}

func TestCanAddAndListProjectDoc(t *testing.T) {
	var _, _ = projectDocProvider.Add([]project_doc_models.ProjectDoc{
		{
			Name:            "TestName",
			SourceCodeUrl:   "/path",
			BuildCommand:    "/run",
			RunCommand:      "123",
			InFiles:         "123",
			OutFiles:        "123",
			ConfigurationId: 1,
			BuildStatus:     "123",
			ArchiveInnerDir: "123",
		},
		{
			Name:            "123",
			SourceCodeUrl:   "123",
			BuildCommand:    "123",
			RunCommand:      "123",
			InFiles:         "123",
			OutFiles:        "123",
			ConfigurationId: 2,
			BuildStatus:     "",
			ArchiveInnerDir: "",
		},
	})

	var l = projectDocProvider.List(&project_doc_models.ProjectDocFilter{
		Ids: projectDocIds,
	})

	if len(l) != 2 {
		t.Fatal("List size must be 2")
	}
	log.Println(l)
	//
	//err = projectDocProvider.Delete(&project_doc_models.ProjectDocDelete{Ids: projectDocIds})
	//if err != nil {
	//	t.Fatal("Can not delete")
	//}
}
*/
