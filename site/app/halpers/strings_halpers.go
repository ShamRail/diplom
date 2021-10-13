package halpers

import "fmt"

func UseCommas(strings []string) {
	for i := range strings {
		strings[i] = "'" + strings[i] + "'"
	}
}

func UseCommasToString(objects []int) []string {
	var res = make([]string, len(objects))
	for i := range objects {
		res[i] = fmt.Sprintf("'%d'", objects[i])
	}
	return res
}
