package stampos

class InstellingController {

    static scaffold = true
	def settingsService
	
	def overzicht()
	{
		return ["allowRequests": settingsService.getAllowRequests(), "allowRequestKeys": [SettingsService.LOCALHOST, SettingsService.LOCAL_NETWORK, SettingsService.EVERYWHERE] ]
	}
	
	def submit()
	{
		settingsService.setAllowRequests(params.allowRequestsFrom)
		flash.message = g.message(code: "settings.saved")
		redirect (action:"overzicht")
	}
}
