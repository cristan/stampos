package stampos

class BeheerController {

    def index() { }
	
	def testDataService
	
	def klantoverzicht()
	{
		def klanten = Klant.findAllWhere(zichtbaar: true);
		if(!klanten)
		{
			klanten = testDataService.getTestKlanten()
		}
		// Dubbelop sorteren omdat de sort in de mapping wordt genegeerd met findAllWhere
		klanten.sort{it.naam}
		return [klanten: klanten]
	}
}
