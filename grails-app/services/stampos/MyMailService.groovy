package stampos

import java.text.DecimalFormat

import org.springframework.context.MessageSource

class MyMailService {

	def klantService
	def mailService
	def grailsApplication
	
	MessageSource messageSource
	
    def sendEmails(alwaysMail)
	{
		def klantenMetEmail = Klant.findAllByZichtbaarAndEmailIsNotNull(true, [sort:"naam"]);
		
		def klantenMetRekening = []
		def klantenMetTegoedGemaild = []
		def klantenMetTegoedNietGemaild = []
		
		Date timeLastMail
		Instelling timeLastMailInstelling = Instelling.findByNaam("TimeLastMail")
		if(timeLastMailInstelling)
		{
			Long time = Long.parseLong(timeLastMailInstelling.getWaarde())
			timeLastMail = new Date(time)
			
			if(!alwaysMail && grailsApplication.config.mail.dont_mail_when_finances_not_uploaded)
			{
				Instelling lastUploadedInstelling = Instelling.findByNaam(UploadController.INITIAL_DATE)
				if(lastUploadedInstelling)
				{
					Date lastUploadedDate = UploadController.sensibleDateFormat.parse(lastUploadedInstelling.waarde);
					if(lastUploadedDate < timeLastMail)
					{
						Object[] parameters = []
						def toLog = messageSource.getMessage("mail.finances_werent_updated", parameters, Locale.default)
						log.info(toLog)
						return [financesWerentUpdated: true]
					}
				}
			}
		}
		
		
		for(Klant klant : klantenMetEmail)
		{
			def tegoed = klantService.tegoed(klant)
			def format = DecimalFormat.getInstance(Locale.FRANCE);
			format.setMinimumFractionDigits(2)
			def klantNaam = klant.naam
			def persoonlijkeUrl = "http://stambar.nl/klantinfo/"+klant.id
			def titel
			def bericht
			boolean mail = false
			if(tegoed < 0)
			{
				klantenMetRekening.add(klant)
				
				mail = true
				Object[] parameters = []
				titel = messageSource.getMessage("mail.insufficientfunds.title", parameters, Locale.default);
				def rekening = format.format(-tegoed)
				parameters = [klantNaam, rekening]
				titel = messageSource.getMessage("mail.insufficientfunds.message", parameters, Locale.default);
			}
			else
			{
				// If the klant is invisible and doesn't have to pay, we assume the customer is no longer relevant
				if(klant.zichtbaar)
				{
					// Check for changes since the last time we mailed
					if(!timeLastMail)
					{
						mail = true
					}
					else
					{
						def countBestellingen = Bestelling.countByKlantAndDatumGreaterThan(klant, timeLastMail)
						if(countBestellingen > 0)
						{
							mail = true
						}
						else
						{
							// When you create a Betaling in the scaffolding, the time portion gets cleared, so it goes back a bit in time.
							// Therefore, we also include any betalingen today regardless of their time
							Date timeLastMailWithoutTime = timeLastMail.clone()
							timeLastMailWithoutTime.clearTime()
							
							def countBetalingen = Betaling.countByKlantAndDatumGreaterThanEquals(klant, timeLastMailWithoutTime)
							if(countBetalingen > 0)
							{
								mail = true
							}
						}
					}
				}
				
				if(!mail)
				{
					klantenMetTegoedNietGemaild.add(klant)
				}
				else
				{
					klantenMetTegoedGemaild.add(klant)
					
					def geformatteerdTegoed = format.format(tegoed)
					Object[] parameters = [geformatteerdTegoed]
					titel = messageSource.getMessage('mail.sufficientfunds.title', parameters, Locale.default);
					
					parameters = [klantNaam, geformatteerdTegoed]
					bericht = messageSource.getMessage('mail.sufficientfunds.message', parameters, Locale.default);
				}
			}
			
			if(mail)
			{
				Object[] parameters = [persoonlijkeUrl]
				bericht += messageSource.getMessage('mail.funds.footer', parameters, Locale.default);
						
				// actually mail
				mailService.sendMail {
					to klant.naam +" <"+klant.email+">"
					from grailsApplication.config.mail.sendername +" <"+ grailsApplication.config.mail.senderaddress +">"
					subject titel
					html bericht.replace("\n", "<br>")
				}
			}
		}
		
		if(timeLastMailInstelling)
		{
			timeLastMailInstelling.setWaarde(String.valueOf(System.currentTimeMillis()))
		}
		else
		{
			timeLastMailInstelling = new Instelling(naam: "TimeLastMail", waarde: String.valueOf(System.currentTimeMillis()))
			timeLastMailInstelling.save()
		}
		
		return [financesWerentUpdated: false, klantenMetRekening: klantenMetRekening, klantenMetTegoedGemaild: klantenMetTegoedGemaild, klantenMetTegoedNietGemaild : klantenMetTegoedNietGemaild]
	}
}
