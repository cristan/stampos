package stampos

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate

class ExampleController {

    def index() {}

    @MessageMapping("/hello")
    @SendTo("/topic/hello")
    protected String hello(String world) {
        return "hello from controller, ${world}!"
    }
	
	def exampleService
	def test()
	{
		exampleService.hello()
		render "Sent via service"
	}
	
	def pushService
	def test2()
	{
		Klant klant1 = Klant.get(1);
		Betaling b = new Betaling(klant: klant1, bedrag: 1.25)
		b.save(true);
		pushService.userUpdated(klant1)
		render klant1.toString() +" updated"
	}
	
}