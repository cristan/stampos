package stampos

import grails.transaction.Transactional
import grails.util.Environment

@Transactional
class SettingsService {
	private static final String ALLOW_REQUESTS = "allowRequests";
	
	public static final String EVERYWHERE = "everywhere";
	public static final String LOCAL_NETWORK = "local_network";
	public static final String LOCALHOST = "localhost"


    def String getSetting(String name, String defaultValue) {
		Instelling setting = Instelling.findByNaam(name)
		if(!setting)
		{
			setting = new Instelling(naam: name, waarde: defaultValue)
			setting.save()
		}
		return setting.waarde
    }
	
	def setValue(String name, String value)
	{
		Instelling automailInstelling = Instelling.findOrCreateByNaam(name);
		automailInstelling.waarde = value;
		automailInstelling.save()
	}
	
	def String getAllowRequests()
	{
		String defaultValue
		if(Environment.current == Environment.TEST)
		{
			defaultValue = EVERYWHERE
		}
		else
		{
			defaultValue = LOCAL_NETWORK
		}
		return getSetting(ALLOW_REQUESTS, defaultValue)
	}
	
	def setAllowRequests(String allowRequests)
	{
		setValue(ALLOW_REQUESTS, allowRequests)
	}
	
	def boolean allRequestsAllowed()
	{
		return getAllowRequests() == EVERYWHERE
	}
	
	def boolean onlyLocalNetworkAllowed()
	{
		return getAllowRequests() == LOCAL_NETWORK
	}
}
