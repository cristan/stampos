package stampos

import grails.transaction.Transactional
import grails.util.Environment

import java.security.MessageDigest
import java.text.DecimalFormat

import org.springframework.context.MessageSource;

@Transactional
class SettingsService {
	private static final String S_ALLOW_REQUESTS = "allowRequests";
	private static final String S_AUTOMAIL = "automail";
	private static final String S_AUTOMAIL_LIST = "automailList";
	private static final String S_DB_BACKUP_ATTACHED_WITH_MAILLIST = "dbBackupAttachedWithMaillist";
	private static final String S_AUTOMAIL_WHEN_FINANCES_NOT_UPDATED = "automailWhenFinancesNotUpdated";
	private static final String S_AUTOMAIL_LIST_RECIPIENT = "automailListRecipient"
	private static final String S_SENDER_NAME = "senderName"
	private static final String S_SENDER_EMAIL = "senderEmail"
	private static final String S_SUFFICIENT_FUNDS_TITLE = "sufficientFundsTitle"
	private static final String S_INSUFFICIENT_FUNDS_TITLE = "insufficientFundsTitle"
	
	public static final String EVERYWHERE = "everywhere";
	public static final String LOCAL_NETWORK = "local_network";
	public static final String LOCALHOST = "localhost"

	def grailsApplication
	MessageSource messageSource


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
	
	def setValue(String name, boolean value)
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
	
	def boolean isDbBackupAttachedWithMaillist()
	{
		return getSetting(S_DB_BACKUP_ATTACHED_WITH_MAILLIST, true)
	}
	
	def setDbBackupAttachedWithMaillist(boolean enabled)
	{
		setValue(S_DB_BACKUP_ATTACHED_WITH_MAILLIST, String.valueOf(enabled))
	}
	
	def boolean isAutomailWhenFinancesNotUpdated()
	{
		return getSetting(S_AUTOMAIL_WHEN_FINANCES_NOT_UPDATED, false)
	}
	
	def setAutomailWhenFinancesNotUpdated(boolean enabled)
	{
		setValue(S_AUTOMAIL_WHEN_FINANCES_NOT_UPDATED, String.valueOf(enabled))
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
	
	private static final String S_SMTP_PORT = "smtpPort"
	
	def String getSmtpPort()
	{
		return getSetting(S_SMTP_PORT, "465")
	}
	
	def setSmtpPort(String value)
	{
		setValue(S_SMTP_PORT, value)
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
	
	private static final String S_SMTP_ENFORCE_SSL = "smtpEnforceSsl"
	
	def boolean isSmtpEnforceSsl()
	{
		return getSetting(S_SMTP_ENFORCE_SSL, true)
	}
	
	def setSmtpEnforceSsl(boolean value)
	{
		setValue(S_SMTP_ENFORCE_SSL, value)
	}
	
	private static final String S_SMTP_USE_TLS = "smtpUseTls"
	
	def boolean isSmtpUseTls()
	{
		return getSetting(S_SMTP_USE_TLS, true)
	}
	
	def setSmtpUseTls(boolean value)
	{
		setValue(S_SMTP_USE_TLS, value)
	}
	
	def boolean isEmailSettingsSet()
	{
		return getSenderName() && getSenderName() && getSmtpHost() && getSmtpPort() && getSmtpPassword() && getSmtpUsername() 
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
	
	
	private static final String S_ADMIN_PASSWORD = "adminPassword"
	
	def getAdminPasswordHash()
	{
		String configuredPasswordHash = grailsApplication.config.admin.passwordhash
		return getSetting(S_ADMIN_PASSWORD, configuredPasswordHash)
	}
	
	def passwordMatches(String password)
	{
		return hash(password) == getAdminPasswordHash()
	}
	
	private String hash(String value)
	{
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(value.getBytes());
		byte[] hash = md.digest();
		return hash.encodeBase64().toString()
	}
	
	def setAdminPassword(String value)
	{
		setValue(S_ADMIN_PASSWORD, hash(value))
	}
	
	def boolean isEmailContentsSettingsSet()
	{
		return getAccountOwner() && getAccountIban() && getServerUrl()
	}
	
	def getSufficientFundsTitleValue()
	{
		def defaultMessage = messageSource.getMessage('mail.sufficientfunds.title', null, Locale.default)
		return getSetting(S_SUFFICIENT_FUNDS_TITLE, defaultMessage)
	}
	
	def getSufficientFundsTitle(def tegoed)
	{
		return String.format(getSufficientFundsTitleValue(), formatMoney(tegoed))
	}
	
	private String formatMoney(def value)
	{
		def format = DecimalFormat.getInstance(Locale.FRANCE);
		format.setMinimumFractionDigits(2)
		return "\u20AC"+format.format(value)
	}
	
	def setSufficientFundsTitle(String value)
	{
		setValue(S_SUFFICIENT_FUNDS_TITLE, value)
	}
	
	def getInsufficientFundsTitleValue()
	{
		def defaultMessage = messageSource.getMessage('mail.insufficientfunds.title', null, Locale.default)
		return getSetting(S_INSUFFICIENT_FUNDS_TITLE, defaultMessage)
	}
	
	def getInsufficientFundsTitle(def tegoed)
	{
		return String.format(getInsufficientFundsTitleValue(), formatMoney(tegoed))
	}
	
	def setInsufficientFundsTitle(String value)
	{
		setValue(S_INSUFFICIENT_FUNDS_TITLE, value)
	}
}
