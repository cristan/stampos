package stampos

import grails.gsp.PageRenderer

import java.text.DateFormat;
import java.text.DecimalFormat
import java.text.SimpleDateFormat

import org.springframework.context.MessageSource

class MyMailService {

	private static final String TIME_LAST_MAIL = "TimeLastMail"

	def klantService
	def messagingService
	def grailsApplication
	def settingsService
	
	MessageSource messageSource
	
    def sendEmails(alwaysMail)
	{
		def klantenMetEmail = Klant.findAllByZichtbaarAndEmailIsNotNull(true, [sort:"naam"]);
		
		def klantenMetRekening = []
		def klantenMetTegoedGemaild = []
		def klantenMetTegoedNietGemaild = []
		
		Date timeLastMail
		Instelling timeLastMailInstelling = Instelling.findByNaam(TIME_LAST_MAIL)
		boolean financesWerentUpdated = false
		if(timeLastMailInstelling)
		{
			Long time = Long.parseLong(timeLastMailInstelling.getWaarde())
			timeLastMail = new Date(time)
			
			if(!alwaysMail && !settingsService.isAutomailWhenFinancesNotUpdated())
			{
				Instelling lastUploadedInstelling = Instelling.findByNaam(UploadController.INITIAL_DATE)
				if(lastUploadedInstelling)
				{
					Date lastUploadedDate = UploadController.sensibleDateFormat.parse(lastUploadedInstelling.waarde);
					if(lastUploadedDate < timeLastMail)
					{
						financesWerentUpdated = true
					}
				}
			}
		}
		if(financesWerentUpdated)
		{
			return [financesWerentUpdated: true]
		}
		else
		{
			for(Klant klant : klantenMetEmail)
			{
				def tegoed = klantService.tegoed(klant)
				def format = DecimalFormat.getInstance(Locale.FRANCE);
				format.setMinimumFractionDigits(2)
				def klantNaam = klant.naam
				def persoonlijkeUrl = settingsService.getServerUrl() +"klantinfo/"+klant.id
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
					parameters = [klantNaam, rekening, getHtmlCompatibleAccountIban(), settingsService.getAccountOwner()]
					bericht = messageSource.getMessage("mail.insufficientfunds.message", parameters, Locale.default);
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
						
						parameters = [klantNaam, geformatteerdTegoed, getHtmlCompatibleAccountIban(), settingsService.getAccountOwner()]
						bericht = messageSource.getMessage('mail.sufficientfunds.message', parameters, Locale.default);
					}
				}
				
				if(mail)
				{
					Object[] parameters = [persoonlijkeUrl]
					bericht += messageSource.getMessage('mail.funds.footer', parameters, Locale.default);
							
					sendMail(klant.email, titel, bericht.replace("\n", "<br>"))
				}
			}
			
			if(timeLastMailInstelling)
			{
				timeLastMailInstelling.setWaarde(String.valueOf(System.currentTimeMillis()))
			}
			else
			{
				timeLastMailInstelling = new Instelling(naam: TIME_LAST_MAIL, waarde: String.valueOf(System.currentTimeMillis()))
				timeLastMailInstelling.save()
			}
			
			return [financesWerentUpdated: false, klantenMetRekening: klantenMetRekening, klantenMetTegoedGemaild: klantenMetTegoedGemaild, klantenMetTegoedNietGemaild : klantenMetTegoedNietGemaild]
		}
	}
	
	/**
	 * Browsers format collections of numbers as a phone number. In order to prevent this,
	 * put span elements around all words 
	 */
	private def getHtmlCompatibleAccountIban()
	{
		String accountIban = settingsService.getAccountIban()
		String[] split = accountIban.split(" ")
		String toReturn = ""
		for(int i = 0; i < split.length; i++)
		{
			String word = split[i]
			toReturn += "<span>"+word+"</span>"
			if(i != split.length - 1)
			{
				toReturn += " ";
			}
		}
		return toReturn
	}
	
	def getMaillist()
	{
		def klanten = Klant.list()
		def toReturn = []

		for(Klant klant : klanten)
		{
			String klantNaam = klant.naam
			BigDecimal tegoed = klantService.tegoed(klant)
			if(klant.zichtbaar || tegoed < 0)
			{
				toReturn.add([naam: klantNaam, tegoed:tegoed])
			}
		}

		return toReturn;
	}
	
	def getMaillistSubject()
	{
		def subject = grailsApplication.config.mail.subject
		def appendDate = grailsApplication.config.mail.appendDate

		if(appendDate)
		{
			DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
			subject = subject +" "+ df.format(new Date())
		}
		return subject
	}
	
	
	PageRenderer groovyPageRenderer
	def sendEmailList()
	{
		def maillist = getMaillist();
		//String html = render(template: "/templates/klantLijst", klantLijst: maillist)
		String renderedMaillist = groovyPageRenderer.render template: '/templates/klantLijst', model: [klantLijst:maillist]
		String htmlMessage="<!DOCTYPE HTML>\n<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n</head>\n<body>";
		htmlMessage += renderedMaillist
		htmlMessage += "</body>\n</html>"
		
		String recipient = settingsService.automailListRecipient
		sendMail(recipient, getMaillistSubject(), htmlMessage)
		
		return recipient
	}
	
	private def sendMail(String to, String subject, String body)
	{
		def props = [
//										"mail.imap.host":"imap.gmail.com",
//										"mail.store.protocol": "imaps",
//										"mail.imap.socketFactory.class": "javax.net.ssl.SSLSocketFactory",
//										"mail.imap.socketFactory.fallback": "false",
//										"mail.imaps.partialfetch": "false",
							"mail.smtp.starttls.enable": String.valueOf(settingsService.isSmtpUseTls()),
							"mail.smtp.host": settingsService.getSmtpHost(),
							"mail.smtp.auth": "true",
							"mail.smtp.socketFactory.port": settingsService.getSmtpPort(),
							"mail.smtp.socketFactory.class": "javax.net.ssl.SSLSocketFactory",
							"mail.smtp.socketFactory.fallback": String.valueOf(!settingsService.isSmtpEnforceSsl())
							]
		
		def map = [
			to: to,
			subject: subject,
			body: body,
			html: true,
			hostname: settingsService.getSmtpHost(),
			username: settingsService.getSmtpUsername(),
			password: settingsService.getSmtpPassword(),
			from: settingsService.getSender(),
			hostProps: props]
		messagingService.sendEmail(map)
	}
}
