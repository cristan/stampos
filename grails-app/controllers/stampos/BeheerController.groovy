package stampos

import grails.util.Environment

class BeheerController {

    def index() { }
	
	def testDataService
	
	def klantoverzicht()
	{
		def klanten = Klant.findAllWhere(zichtbaar: true);
		if(!klanten && Environment.current == Environment.DEVELOPMENT)
		{
			klanten = testDataService.getTestKlanten()
		}
		// Dubbelop sorteren omdat de sort in de mapping wordt genegeerd met findAllWhere
		klanten.sort{it.naam}
		return [klanten: klanten]
	}
}
