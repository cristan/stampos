package stampos

import grails.transaction.Transactional
import grails.util.Environment

@Transactional
class SettingsService {
	private static final String S_ALLOW_REQUESTS = "allowRequests";
	private static final String S_AUTOMAIL = "automail";
	private static final String S_AUTOMAIL_LIST = "automailList";
	private static final String S_AUTOMAIL_LIST_RECIPIENT = "automailListRecipient"
	
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
	
	def boolean getSetting(String name, boolean defaultValue)
	{
		Instelling setting = Instelling.findByNaam(name)
		if(!setting)
		{
			setting = new Instelling(naam: name, waarde: String.valueOf(defaultValue))
			setting.save()
		}
		return setting.waarde.toBoolean()
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
		return getSetting(S_ALLOW_REQUESTS, defaultValue)
	}
	
	def setAllowRequests(String allowRequests)
	{
		setValue(S_ALLOW_REQUESTS, allowRequests)
	}
	
	def boolean isAllRequestsAllowed()
	{
		return getAllowRequests() == EVERYWHERE
	}
	
	def boolean isOnlyLocalNetworkAllowed()
	{
		return getAllowRequests() == LOCAL_NETWORK
	}
	
	def boolean isAutomailEnabled()
	{
		return getSetting(S_AUTOMAIL, false)
	}
	
	def boolean isAutomailListEnabled()
	{
		return getSetting(S_AUTOMAIL_LIST, false)
	}
	
	def setAutomailListEnabled(boolean enabled)
	{
		setValue(S_AUTOMAIL_LIST, String.valueOf(enabled))
	}
	
	def String getAutomailListRecipient()
	{
		return getSetting(S_AUTOMAIL_LIST_RECIPIENT, "")
	}
	
	def setAutomailListRecipient(String recipient)
	{
		return setValue(S_AUTOMAIL_LIST_RECIPIENT, recipient)
	}
}
