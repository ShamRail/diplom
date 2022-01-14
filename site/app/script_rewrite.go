package main

import (
	"io/ioutil"
	"log"
	"strings"
)

func ReplaceStrings(websocket, file string) {

	input, err := ioutil.ReadFile(file)
	if err != nil {
		log.Fatalln(err)
	}

	lines := strings.Split(string(input), "\n")
	changes := 0
	for i, line := range lines {
		if strings.Contains(line, "let host =") {
			lines[i] = "let host = \"" + "http://" + websocket + "/app/\";"
			changes++
		}
		if strings.Contains(line, "let hostRunner =") {
			lines[i] = "let hostRunner =  \"" + "http://" + websocket + "/runner/app/\";"
			changes++
		}
		if changes == 2 {
			break
		}
	}
	output := strings.Join(lines, "\n")
	err = ioutil.WriteFile(file, []byte(output), 0644)
	if err != nil {
		log.Fatalln(err)
	}
}
