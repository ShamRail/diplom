package database_tests

import (
	"site_app/database"
	"testing"
)

var db = database.CreateDataBase("localhost", "5432", "postgres", "postgres", "postgres", "disable")

func TestCanConnectToDataBase(t *testing.T) {
	var dataBase = db
	if dataBase.Db.DB == nil {
		t.Fatal("Not connected")
	}
}
