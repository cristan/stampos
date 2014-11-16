package stampos

import java.security.MessageDigest

class AuthenticationController {

	def index = {
		redirect(action:"login")
	}
	
    def login = {
		String password = params.password
		
		def maintenancePasswordHash = grailsApplication.config.maintenance.passwordhash
		if(!maintenancePasswordHash)
		{
			return [error : "maintenance.passwordhash isn't defined in Config.groovy. It has to be set before you can login.",
				nameOfController: params.nameOfController, nameOfAction: params.nameOfAction]
		}
		
		if(password == null)
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
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(password.getBytes());
			byte[] hash = md.digest();
			def passwordHash = hash.encodeBase64().toString()
			
			if(passwordHash == maintenancePasswordHash)
			{
				session.loggedIn = true;
				if(params.nameOfController && "authentiation" != params.nameOfController)
				{
					flash.message = "Welcome!"
							redirect(controller: params.nameOfController, action: params.nameOfAction)
				}
				else
				{
					flash.message = "You are now logged in"
							redirect(uri: "")
				}
			}
			else
			{
				log.info("Invalid password hash: "+ passwordHash)
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
