package database_tests

import (
	uuid "github.com/satori/go.uuid"
	"log"
	"site_app/database/models/project_doc_models"
	"site_app/database/providers"
	"testing"
)

var projectDocProvider = providers.ProjectDocProvider{DataBase: db}

var projectDocIds = []uuid.UUID{
	uuid.FromStringOrNil("859e6d31-80e2-4002-bef1-9d905815494d"),
	uuid.FromStringOrNil("c1cc13e3-f28c-4201-b302-3bc10866fb6c"),
}

func TestCanAddAndListProjectDoc(t *testing.T) {
	var err = projectDocProvider.Add([]project_doc_models.ProjectDoc{
		{
			Id:              &projectDocIds[0],
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
			Id:              &projectDocIds[1],
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
	if err != nil {
		t.Fatal(err)
	}
	var l = projectDocProvider.List(&project_doc_models.ProjectDocFilter{
		Ids: projectDocIds,
	})

	if len(l) != 2 {
		t.Fatal("List size must be 1")
	}
	log.Println(l)
	//
	//err = projectDocProvider.Delete(&project_doc_models.ProjectDocDelete{Ids: projectDocIds})
	//if err != nil {
	//	t.Fatal("Can not delete")
	//}
}
