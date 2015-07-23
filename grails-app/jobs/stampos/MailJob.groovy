
package stampos

import org.springframework.context.MessageSource



class MailJob {
	def myMailService
	def settingsService
	MessageSource messageSource
	
    static triggers = {
//		cron name:'MailJobTrigger', cronExpression:"0,10,20,30,40,50 * * * * ?"// Every 10 seconds
		cron name:'MailJobTrigger', cronExpression:"0 0 12 ? * SUN *"// Every sunday at 12 o'clock according to cronmaker.com
//		cron name:'MailJobTrigger', cronExpression:"0 12 * * 0 ?"//Every sunday at 12 o'clock according to www.csgnetwork.com/crongen.html
	}
		
	def execute(context) {
		if(settingsService.isAutomailEnabled())
		{
			log.info "Automatisch e-mails versturen.."
			def returned = myMailService.sendEmails(false);
			if(returned.klantenMetRekening)
			{
				log.info message(code: "mail.mailed_to_customers_who_have_to_pay") +":";
				for(klant in returned.klantenMetRekening)
				{
					log.info klant.naam
				}
				
			}
			if(returned.klantenMetTegoedGemaild)
			{
				log.info message(code: "mail.mailed_to_customers_who_dont_have_to_pay") +":";
				for(klant in returned.klantenMetTegoedGemaild)
				{
					log.info klant.naam
				}
				
			}
			if(returned.klantenMetTegoedNietGemaild)
			{
				log.info message(code: "mail.not_mailed_title") +":";
				for(klant in returned.klantenMetTegoedNietGemaild)
				{
					log.info klant.naam
				}
			}
		}
		else
		{
			log.info "Automatisch e-mails versturen staat uit: geen e-mails worden verstuurd"
		}
	  }
	
	def message(Map[] arguments)
	{
		Object[] emptyArgs= []
		return messageSource.getMessage(arguments.code[0], emptyArgs, Locale.default)
	}
	
	
}
