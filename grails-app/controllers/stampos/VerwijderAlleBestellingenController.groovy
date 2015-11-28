package stampos

import java.security.MessageDigest

class VerwijderAlleBestellingenController {

    def index()
	{ 
		
	}
	
	def settingsService
	
	def verwijderd()
	{
		String password = params.password
		if(!settingsService.passwordMatches(password))
		{
			flash.message = "Incorrect wachtwoord!"
			redirect(action:"index")
		}
		else
		{
			def aantalVerwijderd = Bestelling.count
			for(it in Bestelling.all)
			{
				it.delete(flush:true)
			}
			return [aantalVerwijderd: aantalVerwijderd]
		}
	}
	
}
