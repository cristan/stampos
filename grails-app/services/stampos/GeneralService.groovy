package stampos

class GeneralService {
	List<String> localhost_starts = ["127.0.0.1", "0:0:0:0:0:0:0:1"]
	List<String> internal_network_starts = ["192.168", "10.", "fd", "fe"]
	def settingsService

    def isAllowedToOrder(theRequest) {
		boolean isAllowed = false
		if(settingsService.allRequestsAllowed())
		{
			isAllowed = true
		}
		else
		{
			def starts;
			if(settingsService.onlyLocalNetworkAllowed())
			{
				starts = internal_network_starts
				starts.addAll(localhost_starts)
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
					isAllowed = true;
				}
			}
			if(!isAllowed)
			{
				log.info("A request is done by the with remote address: "+ remoteAddress +". This address isn't allowed to make orders.")
			}
		}
		return isAllowed
    }
}
