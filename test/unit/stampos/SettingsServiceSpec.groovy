package stampos

import grails.test.mixin.TestFor
import spock.lang.Specification

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
}
