package stampos

class IndexController {

	def generalService
	
    def index()
	{
		return [allowedToOrder: generalService.isAllowedToOrder(request)]
	}
}
