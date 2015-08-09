package stampos

import java.text.DateFormat
import java.text.SimpleDateFormat
//import org.quartz.Trigger

class MyMailController {

	def klantService
	def myMailService
	def settingsService

	def maillijst() {
		def klantLijst = myMailService.getMaillist();
		return [klantLijst:klantLijst, automailListEnabled : settingsService.isAutomailListEnabled(), recipient : settingsService.automailListRecipient]
	}

	def submitSettings()
	{
		settingsService.setAutomailListEnabled(params.automail != null)
		settingsService.setAutomailListRecipient(params.recipient)

		redirect (action:"maillijst")
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
