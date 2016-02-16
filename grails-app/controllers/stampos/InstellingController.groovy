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
			"titleInCaseOfCredit": settingsService.getSufficientFundsTitleValue(),
			"titleInCaseOfDebit": settingsService.getInsufficientFundsTitleValue(),
			"accountiban": settingsService.getAccountIban(),
			"serverurl": settingsService.getServerUrl() ]
	}
	
	def changePassword()
	{
		
	}
	
	def setPassword()
	{
		if(settingsService.getAdminPasswordHash())
		{
			redirect(action: "changePassword")
		}
	}
	
	def doChangePassword()
	{
		if(!settingsService.passwordMatches(params.currentPassword))
		{
			flash.message = g.message(code: "settings.change_password.current_password_incorrect")
			redirect(action:"changePassword")
		}
		else if(!params.newPassword1)
		{
			flash.message = g.message(code: "settings.change_password.new_password_not_set")
			redirect(action:"changePassword")
		}
		else if(params.newPassword1 != params.newPassword2)
		{
			flash.message = g.message(code: "settings.change_password.passwords_dont_match")
			redirect(action:"changePassword")
		}
		else
		{
			settingsService.setAdminPassword(params.newPassword1)
			flash.message = g.message(code: "settings.change_password.password_changed")
			redirect(action:"overzicht")
		}
	}
	
	def doSetPassword()
	{
		// Security measure: you shouldn't be able to set the password without providing the original one
		// if a password is already present
		if(settingsService.getAdminPasswordHash())
		{
			redirect(action: "changePassword")
		}
		
		if(!params.password1)
		{
			flash.message = g.message(code: "settings.change_password.new_password_not_set")
			redirect(action:"setPassword")
		}
		else if(params.password1 != params.password2)
		{
			flash.message = g.message(code: "settings.change_password.passwords_dont_match")
			redirect(action:"setPassword")
		}
		else
		{
			settingsService.setAdminPassword(params.password1)
			flash.message = g.message(code: "settings.set_password.password_set")
			session.loggedIn = true;
			redirect(controller:"beheer")
		}
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
		settingsService.setSufficientFundsTitle(params.titleInCaseOfCredit)
		settingsService.setInsufficientFundsTitle(params.titleInCaseOfDebit)
		settingsService.setAccountOwner(params.accountowner)
		settingsService.setAccountIban(params.accountiban)
		settingsService.setServerUrl(params.serverurl)
		flash.message = g.message(code: "settings.saved")
		redirect (action:"overzicht")
	}
}
