package stampos

import grails.converters.JSON
import java.security.MessageDigest

class DeblokkeerController {
	
	def klantService
	def pushService
	def settingsService

    def deblokkeer() { }
	
	def logIn()
	{
		String password = params.password
		def loggedIn
		
		def adminPasswordHash = settingsService.getAdminPasswordHash()
		if(!adminPasswordHash)
		{
			loggedIn = false
		}
		else
		{
			if(settingsService.passwordMatches(password))
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
		render result as JSON
	}
	
	def blockedUsers()
	{
		if(!isLoggedIn()) {
			render(status: 401, text: 'Je bent niet ingelogd')
		} else {
			def geblokkeerdeKlanten = []
			def klanten = Klant.findAllWhere(zichtbaar: true)
			klanten.sort{it.naam}
			for(Klant klant : klanten)
			{
				BigDecimal tegoed = klantService.tegoed(klant)
				def geblokkeerd = klantService.geblokkeerd(klant, tegoed)
				if(geblokkeerd)
				{
					Date laatstBetaald = klantService.laatstBetaald(klant)
					geblokkeerdeKlanten.add(["id":klant.id, "naam": klant.naam, "tegoed": tegoed, "laatstBetaald": laatstBetaald, "laatsteToegang": klant.laatsteToegang]);
				}
			}
			render geblokkeerdeKlanten as JSON
		}
	}
	
	def doDeblokkeer()
	{
		if(!isLoggedIn()) {
			render(status: 401, text: 'Je bent niet ingelogd')
		} else {
			int klantId = params.klantId as int
			Klant klant = Klant.get(klantId)
			klant.uitstelTot = klantService.komendeMaandag()
			klant.save(true)
			pushService.userUpdated(klant)
			def toReturn = ["id":klant.id, "naam": klant.naam, "laatsteToegang": klant.laatsteToegang]
			render toReturn as JSON
		}
	}
	
	def isLoggedIn() {
		if(session.loggedIn) {
			return true
		} else {
			def passwordHash = request.getHeader("Password-Hash")
			return passwordHash == settingsService.getAdminPasswordHash()
		}
	}
}
