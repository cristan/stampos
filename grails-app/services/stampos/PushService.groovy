package stampos

import org.springframework.messaging.simp.SimpMessagingTemplate;

import grails.transaction.Transactional

@Transactional
class PushService {

	def klantInfoService
    SimpMessagingTemplate brokerMessagingTemplate
	
	void userUpdated(Klant klant) {
		brokerMessagingTemplate.convertAndSend "/topic/user", klant.id
	}
	
	void orderDone(Bestelling bestelling)
	{
		def item = klantInfoService.getJsonOrder(bestelling, true)
		brokerMessagingTemplate.convertAndSend "/topic/order", item
	}
}
