package halpers

func UseCommas(strings []string) {
	for i := range strings {
		strings[i] = "'" + strings[i] + "'"
	}
}
