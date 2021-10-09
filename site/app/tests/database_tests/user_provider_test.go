package database_tests

import (
	"github.com/satori/go.uuid"
	"log"
	"site_app/database/models"
	"site_app/database/providers"
	"testing"
)

var userProvider = providers.UserProvider{DataBase: db}

var uuidList = []uuid.UUID{
	uuid.FromStringOrNil("859e6d31-80e2-4002-bef1-9d905815494d"),
	uuid.FromStringOrNil("c1cc13e3-f28c-4201-b302-3bc10866fb6c"),
	uuid.FromStringOrNil("0b2e5ae6-2307-4055-bf46-a54916250701"),
	uuid.FromStringOrNil("8d984944-b29a-45be-ae46-49bfe13df1c3"),
}

func TestCanAddUser(t *testing.T) {
	userProvider.Add([]models.User{
		{Id: &uuidList[0], Name: "Andrew", Password: "1234"},
		{Id: &uuidList[1], Name: "Alex", Password: "23456"},
		{Id: &uuidList[2], Name: "Moris", Password: "45456"},
		{Id: &uuidList[3], Name: "Jack", Password: "456456"},
	})
}

func TestCanListUser(t *testing.T) {
	var users = userProvider.List(nil)
	if len(users) != 4 {
		t.Fatal("Tests users count should be 4")
	}
	log.Println(len(users))
}

func TestCanListFilterUser(t *testing.T) {
	var users []models.User
	users = userProvider.List(&models.UserFilter{
		Ids:       []uuid.UUID{uuidList[0]},
		Names:     []string{"Andrew"},
		Passwords: []string{"1234"},
	})
	if len(users) != 1 {
		t.Fatal("Tests users count should be 1")
	}

	users = userProvider.List(&models.UserFilter{
		Names: []string{"Alex"},
	})
	if len(users) != 1 {
		t.Fatal("Tests users count should be 1")
	}

	users = userProvider.List(&models.UserFilter{
		Ids: uuidList,
	})
	if len(users) != 4 {
		t.Fatal("Tests users count should be 4")
	}

	users = userProvider.List(&models.UserFilter{
		Ids:   uuidList,
		Names: []string{"Alex", "Andrew"},
	})
	if len(users) != 2 {
		t.Fatal("Tests users count should be 2")
	}
}

func TestCanUpdateUser(t *testing.T) {
	userProvider.Update(models.User{
		Id:       &uuidList[0],
		Name:     "AndrewUpdated",
		Password: "22121231",
	})
	var users = userProvider.List(&models.UserFilter{
		Ids: []uuid.UUID{uuidList[0]},
	})
	if users[0].Name != "AndrewUpdated" || users[0].Password != "22121231" {
		t.Fatal("Not updated")
	}
	if len(users) != 1 {
		t.Fatal("Tests users count should be 1")
	}
}

func TestCanDeleteUser(t *testing.T) {
	var deleteUser = models.UserDelete{
		Ids: uuidList,
	}
	userProvider.Delete(&deleteUser)
}
