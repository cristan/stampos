package stampos

import org.springframework.messaging.simp.SimpMessagingTemplate;

import grails.transaction.Transactional

@Transactional
class PushService {

    SimpMessagingTemplate brokerMessagingTemplate
	
	void userUpdated(Klant klant) {
		brokerMessagingTemplate.convertAndSend "/topic/user", klant.id
	}
}
