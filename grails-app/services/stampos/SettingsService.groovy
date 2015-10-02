package stampos

import grails.transaction.Transactional
import grails.util.Environment

@Transactional
class SettingsService {
	private static final String S_ALLOW_REQUESTS = "allowRequests";
	private static final String S_AUTOMAIL = "automail";
	private static final String S_AUTOMAIL_LIST = "automailList";
	private static final String S_AUTOMAIL_LIST_RECIPIENT = "automailListRecipient"
	private static final String S_SENDER_NAME = "senderName"
	private static final String S_SENDER_EMAIL = "senderEmail"
	
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
	
	def setSenderName(String senderName)
	{
		setValue(S_SENDER_NAME, senderName)
	}
	
	def String getSenderName()
	{
		return getSetting(S_SENDER_NAME, "")
	}
	
	def setSenderEmail(String senderEmail)
	{
		setValue(S_SENDER_EMAIL, senderEmail)
	}
	
	def String getSenderEmail()
	{
		return getSetting(S_SENDER_EMAIL, "")
	}
	
	def String getSender()
	{
		getSenderName() +" <"+ getSenderEmail() +">"
	}
	
	private static final String S_SMTP_HOST = "smtpHost"
	
	def String getSmtpHost()
	{
		return getSetting(S_SMTP_HOST, "")
	}
	
	def setSmtpHost(String value)
	{
		setValue(S_SMTP_HOST, value)
	}
	
	private static final String S_SMTP_USERNAME = "smtpUsername"
	
	def String getSmtpUsername()
	{
		return getSetting(S_SMTP_USERNAME, "")
	}
	
	def setSmtpUsername(String value)
	{
		setValue(S_SMTP_USERNAME, value)
	}
	
	private static final String S_SMTP_PASSWORD = "smtpPassword"
	
	def String getSmtpPassword()
	{
		return getSetting(S_SMTP_PASSWORD, "")
	}
	
	def setSmtpPassword(String value)
	{
		setValue(S_SMTP_PASSWORD, value)
	}
	
	private static final String S_ACCOUNT_OWNER = "accountOwner"
	
	def String getAccountOwner()
	{
		return getSetting(S_ACCOUNT_OWNER, "")
	}
	
	def setAccountOwner(String value)
	{
		setValue(S_ACCOUNT_OWNER, value)
	}
	
	private static final String S_ACCOUNT_IBAN = "accountIban"
	
	def String getAccountIban()
	{
		return getSetting(S_ACCOUNT_IBAN, "")
	}
	
	def setAccountIban(String value)
	{
		setValue(S_ACCOUNT_IBAN, value)
	}
	
	private static final String S_SERVER_URL = "serverUrl"
	
	def String getServerUrl()
	{
		String serverUrl = getSetting(S_SERVER_URL, "")
		if(serverUrl)
		{
			if(!serverUrl.startsWith("http://") && !serverUrl.startsWith("https://"))
			{
				serverUrl = "http://"+ serverUrl
			}
			if(!serverUrl.endsWith("/"))
			{
				serverUrl += "/"
			}
		}
		return serverUrl
	}
	
	def setServerUrl(String value)
	{
		setValue(S_SERVER_URL, value)
	}
}
