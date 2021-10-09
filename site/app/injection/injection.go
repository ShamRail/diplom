package injection

import (
	"site_app/database/providers"
)

type Injection struct {
	UserProvider *providers.UserProvider
}
