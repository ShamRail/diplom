package injection

import (
	"cite_app/database/providers"
)

type Injection struct {
	UserProvider *providers.UserProvider
}
