package stampos

import grails.converters.JSON
import java.security.MessageDigest

class DeblokkeerController {
	
	def klantService

    def deblokkeer() { }
	
	def logIn()
	{
		String password = params.password
		def loggedIn
		
		def maintenancePasswordHash = grailsApplication.config.maintenance.passwordhash
		if(!maintenancePasswordHash)
		{
			loggedIn = false
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
				loggedIn = true
			}
			else
			{
				loggedIn = false;
			}
		}
		
		def result = [loggedIn: loggedIn]
		render "${params.callback}(${result as JSON})"
	}
	
	def getBlockedUsers()
	{
		def geblokkeerdeKlanten = []
		def klanten = Klant.findAllWhere(zichtbaar: true)
		klanten.sort{it.naam}
		for(Klant klant : klanten)
		{
			def geblokkeerd = klantService.geblokkeerd(klant)
			if(geblokkeerd)
			{
				geblokkeerdeKlanten.add(klant);
			}
		}
		render "${params.callback}(${geblokkeerdeKlanten as JSON})"
	}
	
	def doDeblokkeer()
	{
		int klantId = params.klantId as int
		Klant klant = Klant.get(klantId)
		klant.uitstelTot = klantService.komendeMaandag()
		klant.save(true)
		render "${params.callback}()"
	}
}
