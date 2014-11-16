package stampos

import grails.util.Environment

class GeneralService {
	def starts = ["127.0.0.1", "0:0:0:0:0:0:0:1"]

    def isAllowedToOrder(theRequest) {
		String environmentName = Environment.current.name
		
		if(environmentName != "production")
		{
			return true;
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
