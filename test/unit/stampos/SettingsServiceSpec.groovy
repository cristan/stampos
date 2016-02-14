package stampos

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.support.GrailsUnitTestMixin
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(SettingsService)
class SettingsServiceSpec extends Specification {
	static transactional = true
	
    def setup() {
		mockDomain(Instelling)
    }

    def cleanup() {
    }

    void "test iban"() {
		given:
			
		when:
			String testIban = "NL12 189 1273 1278"
			service.setAccountIban(testIban)
		then:
			assertEquals(testIban, service.getAccountIban())
    }
	
	void "test title sufficient funds default"() {
		given:	
		when:
			messageSource.addMessage("mail.sufficientfunds.title", Locale.default, "Your credit: %s")
		then:
			assertEquals("Your credit: \u20AC2,20", service.getSufficientFundsTitle(2.2))
	}
	
	void "test title sufficient funds set"() {
		given:
		when:
			messageSource.addMessage("mail.sufficientfunds.title", Locale.default, "Your credit: %s")
			service.setSufficientFundsTitle("Due: %s")
			
		then:
			assertEquals("Due: \u20AC2,20", service.getSufficientFundsTitle(2.2))
	}
	
	void "test title insufficient funds default"() {
		given:
		when:
			messageSource.addMessage("mail.insufficientfunds.title", Locale.default, "Your debit: %s")
		then:
			assertEquals("Your debit: \u20AC2,20", service.getInsufficientFundsTitle(2.2))
	}
	
	void "test title insufficient funds set"() {
		given:
		when:
			messageSource.addMessage("mail.insufficientfunds.title", Locale.default, "Your debit: %s")
			service.setInsufficientFundsTitle("To pay: %s")
			
		then:
			assertEquals("To pay: \u20AC2,20", service.getInsufficientFundsTitle(2.2))
	}
}
