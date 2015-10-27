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
		def emailSettingsSet = settingsService.isEmailSettingsSet()
		return [klantLijst:klantLijst, automailListEnabled : settingsService.isAutomailListEnabled(), recipient : settingsService.automailListRecipient, emailSettingsSet: emailSettingsSet]
	}

	def submitSettings()
	{
		settingsService.setAutomailListEnabled(params.automail != null)
		settingsService.setAutomailListRecipient(params.recipient)

		redirect (action:"maillijst")
	}

	def versturen()
	{
		def klantenMetEmail = Klant.findAllByZichtbaarAndEmailIsNotNull(true, [sort:"naam"])
		def klantenZonderEmail = Klant.findAllByZichtbaarAndEmailIsNull(true, [sort:"naam"])
		Instelling automailInstelling = Instelling.findByNaam("automail")
		def automailEnabled = automailInstelling && automailInstelling.waarde.toBoolean()
		def automailWhenFinancesNotUpdated = settingsService.isAutomailWhenFinancesNotUpdated()
		def emailSettingsSet = settingsService.isEmailSettingsSet() && settingsService.isEmailContentsSettingsSet()
		return [metMail : klantenMetEmail, zonderMail: klantenZonderEmail, automailEnabled: automailEnabled, emailSettingsSet: emailSettingsSet, automailWhenFinancesNotUpdated: automailWhenFinancesNotUpdated]
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
	
	def disableAutomailWhenFinancesNotUpdated()
	{
		settingsService.setAutomailWhenFinancesNotUpdated(false)
		redirect(action:"versturen")
	}
	
	def enableAutomailWhenFinancesNotUpdated()
	{
		settingsService.setAutomailWhenFinancesNotUpdated(true)
		redirect(action:"versturen")
	}
}
