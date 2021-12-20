package services

import "time"

type BuildStatusResponse struct {
	Id            int       `json:"id"`
	StartAt       time.Time `json:"startAt"`
	EndAt         time.Time `json:"endAt"`
	BuildStatus   string    `json:"buildStatus"`
	Message       string    `json:"message"`
	DockerLogPath *string   `json:"dockerLogPath"`
}
