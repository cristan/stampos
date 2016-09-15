package stampos


class AuthenticationController {

	def settingsService
	
	def index = {
		redirect(action:"login")
	}
	
    def login = {
		String password = params.password
		
		def adminPasswordHash = settingsService.getAdminPasswordHash()
		if(!adminPasswordHash)
		{
			flash.message = g.message(code: "settings.change_password.set_admin_password_now")
			redirect(controller:"instelling",action:"setPassword")
		}
		else if(password == null)
		{
			if(session.loggedIn)
			{
				flash.message = "You are already logged in. Go to /logout to log out"
				return [nameOfController: params.nameOfController, nameOfAction: params.nameOfAction]
			}
			else
			{
				// Default situation before logging in: just return nameOfController so it survives the extra request 
				return [nameOfController: params.nameOfController, nameOfAction: params.nameOfAction]
			}
		}
		else
		{
			if(settingsService.passwordMatches(password))
			{
				session.loggedIn = true;
				flash.message = "You are now logged in"
				if(params.nameOfController && params.nameOfController != "authentication")
				{
					redirect(controller: params.nameOfController, action: params.nameOfAction)
				}
				else
				{
					redirect(uri: "")
				}
			}
			else
			{
				return [error : "Invalid password. Please try again.", nameOfController: params.nameOfController, nameOfAction: params.nameOfAction]
			}
		}
	}
	
	def logout =
	{
		session.loggedIn = false;
		flash.message = "You are now logged out"
		redirect(action:"login", params: [nameOfController: params.nameOfController, nameOfAction: params.nameOfAction])
	}
}
