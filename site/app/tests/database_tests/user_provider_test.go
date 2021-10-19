package database_tests

import (
	"fmt"
	"github.com/satori/go.uuid"
	"log"
	"site_app/database/models/user_models"
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
	userProvider.Add([]user_models.User{
		{Id: &uuidList[0], Name: "Andrew", Password: []byte("03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4")},
		{Id: &uuidList[1], Name: "Alex", Password: []byte("9b56ca8566a48b98a8c29a7fd307038ed555123439a937eb85d9c45166881e6e")},
		{Id: &uuidList[2], Name: "Moris", Password: []byte("fbed36db6cac99763e3d6dd0cc5f4ca1cf08f83ec3d1a07c190d738ec1640884")},
		{Id: &uuidList[3], Name: "Jack", Password: []byte("54bb6a0d2ea7d49744e886aa20859d70b6fc4ee0b9f144353ecb4b39195767f3")},
	})

	var users = userProvider.List(&user_models.UserFilter{
		Ids: uuidList,
	})
	if len(users) != 4 {
		t.Fatal("Tests user_models count should be 4")
	}
}

func TestCanListUser(t *testing.T) {
	var users = userProvider.List(nil)
	if len(users) != 4 {
		t.Fatal("Tests user_models count should be 4")
	}
	log.Println(len(users))
}

func TestCanListFilterUser(t *testing.T) {
	var users []user_models.User
	users = userProvider.List(&user_models.UserFilter{
		Ids:   []uuid.UUID{uuidList[0]},
		Names: []string{"Andrew"},
	})
	if len(users) != 1 {
		t.Fatal("Tests user_models count should be 1")
	}

	users = userProvider.List(&user_models.UserFilter{
		Names: []string{"Alex"},
	})
	if len(users) != 1 {
		t.Fatal("Tests user_models count should be 1")
	}

	users = userProvider.List(&user_models.UserFilter{
		Ids: uuidList,
		Emails: []string{
			"", "",
		},
	})
	if len(users) != 4 {
		t.Fatal("Tests user_models count should be 4")
	}

	users = userProvider.List(&user_models.UserFilter{
		Ids:   uuidList,
		Names: []string{"Alex", "Andrew"},
	})
	if len(users) != 2 {
		t.Fatal("Tests user_models count should be 2")
	}
}

func TestCanUpdateUser(t *testing.T) {
	userProvider.Update(user_models.User{
		Id:       &uuidList[0],
		Name:     "AndrewUpdated",
		Password: []byte("55e224a9c692c5874ea7b12543e8e3e29047ce41c037fceb788de1a430d1bbe2"),
	})
	var users = userProvider.List(&user_models.UserFilter{
		Ids: []uuid.UUID{uuidList[0]},
	})
	var bytes = []byte("55e224a9c692c5874ea7b12543e8e3e29047ce41c037fceb788de1a430d1bbe2")
	if users[0].Name != "AndrewUpdated" || fmt.Sprint(users[0].Password) != fmt.Sprint(bytes) {
		t.Fatal("Not updated")
	}
	if len(users) != 1 {
		t.Fatal("Tests user_models count should be 1")
	}
}

func TestCanDeleteUser(t *testing.T) {
	var deleteUser = user_models.UserDelete{
		Ids: uuidList,
	}
	userProvider.Delete(&deleteUser)
	var users = userProvider.List(&user_models.UserFilter{
		Ids: uuidList,
	})
	if len(users) != 0 {
		t.Fatal("Tests user_models count should be 0")
	}
}
