package stampos

import java.security.MessageDigest

class VerwijderAlleBestellingenController {

    def index()
	{ 
		
	}
	
	def verwijderd()
	{
		String password = params.password
		
		def maintenancePasswordHash = grailsApplication.config.maintenance.passwordhash
		
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(password.getBytes());
		byte[] hash = md.digest();
		def passwordHash = hash.encodeBase64().toString()
			
		if(passwordHash != maintenancePasswordHash)
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
