package stampos

import grails.util.GrailsUtil;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat
//import org.quartz.Trigger

class MyMailController {

	def klantService
	def grailsApplication
	def myMailService
	
	static DateFormat format = new SimpleDateFormat("dd-MM-yyyy")
	
    def maillijst() {
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
		
		def to = grailsApplication.config.mail.to
		def subject = grailsApplication.config.mail.subject
		def appendDate = grailsApplication.config.mail.appendDate
		
		if(appendDate)
		{
			subject = subject +" "+ format.format(new Date())
		}
		
		return [klantLijst:toReturn, to:to, subject:subject]
	}
	
	def versturen()
	{
		def klantenMetEmail = Klant.findAllByZichtbaarAndEmailIsNotNull(true, [sort:"naam"]);
		def klantenZonderEmail = Klant.findAllByZichtbaarAndEmailIsNull(true, [sort:"naam"]);
		Instelling automailInstelling = Instelling.findByNaam("automail");
		def automailEnabled = automailInstelling && automailInstelling.waarde.toBoolean();
		return [metMail : klantenMetEmail, zonderMail: klantenZonderEmail, automailEnabled: automailEnabled]
	}
	
	def doVerstuur()
	{
		return myMailService.sendEmails(true)
	}
	
	def enableAutomail()
	{
		Instelling automailInstelling = Instelling.findOrCreateByNaam("automail");
		automailInstelling.waarde = true;
		automailInstelling.save()
		redirect(action:"versturen")
	}
	
	def disableAutomail()
	{
		Instelling automailInstelling = Instelling.findOrCreateByNaam("automail");
		automailInstelling.waarde = false;
		automailInstelling.save()
		redirect(action:"versturen")
	}
}
