package database_tests

import (
	uuid "github.com/satori/go.uuid"
	m "site_app/database/models/user_project_models"
	"site_app/database/providers"
	"testing"
)

var userProject = providers.UserProjectProvider{DataBase: db}

var uuidListP = []uuid.UUID{
	uuid.FromStringOrNil("e2322e4f-d76b-4bcf-b736-4aa138d5818b"),
	uuid.FromStringOrNil("4788197e-3b28-4685-b37b-ea6fe8c06ee9"),
	uuid.FromStringOrNil("f2e144c4-6198-4adc-9c39-258125bffcb5"),

	uuid.FromStringOrNil("c2080805-7676-47b1-8628-f93b64feec69"),
	uuid.FromStringOrNil("bf99b6ce-b83c-414d-8137-dbda767018ad"),
	uuid.FromStringOrNil("31e94497-046f-4fc4-a405-29a69cb9b546"),
}

func TestCanAddUserProjectDoc(t *testing.T) {
	var err = userProject.Add([]m.UserProject{
		{Id: &uuidListP[0], UserId: &uuidListP[1], ProjectId: &uuidListP[2]},
		{Id: &uuidListP[3], UserId: &uuidListP[4], ProjectId: &uuidListP[5]},
	})
	if err != nil {
		t.Fatal("Can not add")
	}
}

func TestCanListUserProjectDoc(t *testing.T) {
	var uuids = []uuid.UUID{
		uuid.FromStringOrNil("e2322e4f-d76b-4bcf-b736-4aa138d5818b"),
		uuid.FromStringOrNil("c2080805-7676-47b1-8628-f93b64feec69"),
	}
	var list = userProject.List(&m.UserProjectFilter{
		Ids: uuids,
	})

	if (list == nil || len(list) == 0) ||
		(list[0].Id.String() != uuidListP[0].String()) ||
		(list[0].UserId.String() != uuidListP[1].String()) ||
		(list[0].ProjectId.String() != uuidListP[2].String()) {
		t.Fatal("Can not list")
	}

	uuids = []uuid.UUID{
		uuid.FromStringOrNil("4788197e-3b28-4685-b37b-ea6fe8c06ee9"),
		uuid.FromStringOrNil("bf99b6ce-b83c-414d-8137-dbda767018ad"),
	}
	list = userProject.List(&m.UserProjectFilter{
		UserIds: uuids,
	})
	if (list == nil || len(list) == 0) ||
		(list[0].Id.String() != uuidListP[0].String()) ||
		(list[0].UserId.String() != uuidListP[1].String()) ||
		(list[0].ProjectId.String() != uuidListP[2].String()) {
		t.Fatal("Can not list")
	}

	uuids = []uuid.UUID{
		uuid.FromStringOrNil("f2e144c4-6198-4adc-9c39-258125bffcb5"),
		uuid.FromStringOrNil("31e94497-046f-4fc4-a405-29a69cb9b546"),
	}
	list = userProject.List(&m.UserProjectFilter{
		ProjectIds: uuids,
	})
	if (list == nil || len(list) == 0) ||
		(list[0].Id.String() != uuidListP[0].String()) ||
		(list[0].UserId.String() != uuidListP[1].String()) ||
		(list[0].ProjectId.String() != uuidListP[2].String()) {
		t.Fatal("Can not list")
	}
}

func TestCanDeleteUserProjectDoc(t *testing.T) {
	var uuids = []uuid.UUID{
		uuid.FromStringOrNil("e2322e4f-d76b-4bcf-b736-4aa138d5818b"),
		uuid.FromStringOrNil("c2080805-7676-47b1-8628-f93b64feec69"),
	}
	var err = userProject.Delete(&m.UserProjectDelete{
		Ids: uuids,
	})
	if err != nil {
		t.Fatal("Can not delete")
	}
}
