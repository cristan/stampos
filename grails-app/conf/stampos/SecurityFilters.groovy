package stampos

class SecurityFilters {

    def filters = {
        loginCheck(controller: '*',  action: "list|show|edit|delete|create|save|update|registreerInkopen|verwerkInkopen|mail|geteld|schatting|registreerTelling|verwerkTelling|doUpload|upload|verwerkActies|uitstel|geefUitstel|doDeblokkeer|overzicht|maillijst|versturen|doVerstuur|klantoverzicht", invert:false) {
            before = {
				if (!session.loggedIn)
				{
					// TODO: parameters: params (except without controllerName and actionName) and fix AuthenticationController to parse the params
                    redirect(controller: 'authentication', action: 'login', params: [nameOfController: controllerName, nameOfAction : actionName])
                    return false
                }
            }
        }
		
        filterInitializer()
		{
			before = {
				def session = request.session
			}
		}
    }
}
