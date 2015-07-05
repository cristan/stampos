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
	
}