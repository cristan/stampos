package stampos

import org.springframework.messaging.simp.SimpMessagingTemplate;

import grails.transaction.Transactional

@Transactional
class ExampleService {

    SimpMessagingTemplate brokerMessagingTemplate
	
	void hello() {
		brokerMessagingTemplate.convertAndSend "/topic/hello", "hello from service!"
	}
}
