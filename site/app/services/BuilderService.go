package services

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	. "site_app/database/models/project_description"
	"site_app/database/models/project_doc_models"
)

type BuilderService struct {
	Config Config
}

func (s *BuilderService) Build(p project_doc_models.ProjectDoc) (string, error) {
	client := http.Client{}

	var body, err = json.Marshal(p)

	if err != nil {
		log.Println(err)
	}

	resp, err := client.Post(s.Config.BuilderApi+"/build", "application/json", bytes.NewBuffer(body))
	if err != nil {
		fmt.Println(err)
		return "", err
	}

	defer resp.Body.Close()
	data, _ := ioutil.ReadAll(resp.Body)

	return string(data), nil
}

func (s *BuilderService) GetBuildStatus(id string) (string, error) {
	client := http.Client{}
	resp, err := client.Get(s.Config.BuilderApi + "/status" + "?projectID=" + id)
	if err != nil {
		fmt.Println(err)
		return "", err
	}

	defer resp.Body.Close()
	data, _ := ioutil.ReadAll(resp.Body)

	return string(data), nil
}

func (s *BuilderService) GetConfigurationAll() (string, error) {

	client := http.Client{}
	resp, err := client.Get(s.Config.AdminApi + "/configuration/all")
	if err != nil {
		fmt.Println(err)
		return "", err
	}

	defer resp.Body.Close()
	data, _ := ioutil.ReadAll(resp.Body)

	return string(data), nil
}
