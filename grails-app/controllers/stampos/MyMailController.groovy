package stampos

import java.text.DateFormat
import java.text.SimpleDateFormat

class MyMailController {

	def klantService
	def myMailService
	def settingsService
	def grailsApplication

	def maillijst() {
		def klantLijst = myMailService.getMaillist();
		def emailSettingsSet = settingsService.isEmailSettingsSet()
		def attachDbBackup = settingsService.isDbBackupAttachedWithMaillist()
		boolean memoryDatabase = grailsApplication.config.dataSource.url.contains("mem")
		return [klantLijst:klantLijst, automailListEnabled : settingsService.isAutomailListEnabled(), memoryDatabase: memoryDatabase,  attachDbBackup: attachDbBackup, recipient : settingsService.automailListRecipient, emailSettingsSet: emailSettingsSet]
	}

	def submitSettings()
	{
		settingsService.setAutomailListEnabled(params.automail != null)
		settingsService.setAutomailListRecipient(params.recipient)
		settingsService.setDbBackupAttachedWithMaillist(params.attachDbBackup != null)
		flash.message = "De instellingen zijn aangepast"
		redirect (action:"maillijst")
	}
	
	def mailNow()
	{
		if(!params.recipient)
		{
			flash.message = "Geen e-mail adres ingevuld!"
		}
		else
		{
			String recipient = params.recipient
			boolean attachDbBackup = params.attachDbBackup != null
			myMailService.sendEmailList(recipient, attachDbBackup)
			flash.message = "Maillijst verstuurd naar ${recipient} "+ (attachDbBackup ? "met": "zonder") +" een database backup als bijlage."
		}
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
