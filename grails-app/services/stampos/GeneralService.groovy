package stampos

import grails.util.Environment

class GeneralService {
	def localhost_starts = ["127.0.0.1", "0:0:0:0:0:0:0:1"]
	def internal_network_starts = ["192.168", "10.", "fd"]
	def settingsService

    def isAllowedToOrder(theRequest) {
		String environmentName = Environment.current.name
		if(environmentName != "production")
		{
			return true;
		}
		else if(settingsService.allRequestsAllowed())
		{
			return true
		}
		else
		{
			def starts;
			if(settingsService.onlyLocalNetworkAllowed())
			{
				starts = internal_network_starts
			}
			else
			{
				starts = localhost_starts
			}
			def remoteAddress = theRequest.getRemoteAddr()
			for(def start : starts)
			{
				if(remoteAddress.startsWith(start))
				{
					return true;
				}
			}
			return false;
		}
    }
}
