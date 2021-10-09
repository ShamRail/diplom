package halpers

import uuid "github.com/satori/go.uuid"

func UuidsToStrings(uuids []uuid.UUID) []string {
	var strings []string
	for i := range uuids {
		strings = append(strings, uuids[i].String())
	}
	return strings
}
