package helpers

import (
	"fmt"
	"site_app/database/models/project_description"
)

func UseCommas(strings []string) {
	for i := range strings {
		strings[i] = "'" + strings[i] + "'"
	}
}

func UseCommasIntsToString(objects []int) []string {
	var res = make([]string, len(objects))
	for i := range objects {
		res[i] = fmt.Sprintf("%d", objects[i])
	}
	return res
}

func UseCommasStatusesToString(objects []project_description.Status) []string {
	var res = make([]string, len(objects))
	for i := range objects {
		res[i] = fmt.Sprintf("'%d'", objects[i])
	}
	return res
}
