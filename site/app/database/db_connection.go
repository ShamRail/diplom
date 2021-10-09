package database

import (
	"fmt"
	"github.com/jmoiron/sqlx"
	_ "github.com/lib/pq"
	"log"
)

type IDataBase interface {
	Connect(host, port, user, password, dbName, sslmode string)
	IsConnected() bool
}

type DataBase struct {
	Db *sqlx.DB
}

func (d *DataBase) Connect(host, port, user, password, dbName, sslmode string) {
	var connectionString = fmt.Sprintf("host=%s port=%s user=%s password=%s dbname=%s sslmode=%s",
		host,
		port,
		user,
		password,
		dbName,
		sslmode)

	db, err := sqlx.Connect("postgres", connectionString)
	d.Db = db
	if err != nil {
		log.Fatalln(err)
	}
}

func (d *DataBase) IsConnected() bool {
	return d.Db != nil
}

func CreateDataBase(host, port, user, password, dbName, sslmode string) *DataBase {
	var db = DataBase{}
	db.Connect(host, port, user, password, dbName, sslmode)
	return &db
}
