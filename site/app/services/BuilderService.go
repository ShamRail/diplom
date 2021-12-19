package services

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	. "site_app/database/models/project_description"
	"strings"
)

type BuilderService struct {
	Config Config
}

func (s *BuilderService) Build(p ProjectDescription) error {
	client := http.Client{}

	var body, err = json.Marshal(p)
	if err != nil {
		log.Println(err)
	}

	resp, err := client.Post("http://"+s.Config.BuilderURL+"/build", "application/json", bytes.NewBuffer(body))
	if err != nil {
		fmt.Println(err)
		return err
	}

	defer resp.Body.Close()
	buf := new(strings.Builder)
	_, err = io.Copy(buf, resp.Body)
	if err != nil {
		fmt.Println(err)
		return err
	}
	return nil
}
