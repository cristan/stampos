package stampos

class InstellingController {

    static scaffold = true
	def settingsService
	
	def overzicht()
	{
		return ["allowRequests": settingsService.getAllowRequests(), 
			"allowRequestKeys": [SettingsService.LOCALHOST, SettingsService.LOCAL_NETWORK, SettingsService.EVERYWHERE],
			"sendername": settingsService.getSenderName(),
			"senderemail": settingsService.getSenderEmail(),
			"smtphost": settingsService.getSmtpHost(),
			"smtpport": settingsService.getSmtpPort(),
			"smtp_require_ssl": settingsService.isSmtpEnforceSsl(),
			"smtp_use_tls": settingsService.isSmtpUseTls(),
			"smtpusername": settingsService.getSmtpUsername(),
			"smtppassword": settingsService.getSmtpPassword(),
			"accountowner": settingsService.getAccountOwner(),
			"accountiban": settingsService.getAccountIban(),
			"serverurl": settingsService.getServerUrl() ]
	}
	
	def submit()
	{
		settingsService.setAllowRequests(params.allowRequestsFrom)
		flash.message = g.message(code: "settings.saved")
		redirect (action:"overzicht")
	}
	
	def submitEmail()
	{
		settingsService.setSenderName(params.sendername)
		settingsService.setSenderEmail(params.senderemail)
		settingsService.setSmtpHost(params.smtphost)
		settingsService.setSmtpPort(params.smtpport)
		settingsService.setSmtpEnforceSsl("on".equals(params.smtp_require_ssl))
		settingsService.setSmtpUseTls("on".equals(params.smtp_use_tls))
		settingsService.setSmtpUsername(params.smtpusername)
		settingsService.setSmtpPassword(params.smtppassword)
		settingsService.setAccountOwner(params.accountowner)
		settingsService.setAccountIban(params.accountiban)
		settingsService.setServerUrl(params.serverurl)
		flash.message = g.message(code: "settings.saved")
		redirect (action:"overzicht")
	}
}
