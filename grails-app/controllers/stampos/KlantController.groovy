package stampos

import grails.converters.JSON
import grails.util.Environment

class KlantController {

	static scaffold = true
	
	def klantService
	def testDataService
	
	
	def all()
	{
		def toReturn = [];
		for(Klant klant : getAllKlanten())
		{
			toReturn.add(id: klant.id, naam: klant.naam);
		}
		
		render "${params.callback}(${toReturn as JSON})"
	}
	
	private List<Klant> getAllKlanten()
	{ 
		List<Klant> result = Klant.findAllByZichtbaar(true, [sort:"naam"]);
		if(!result && Environment.current != Environment.PRODUCTION)
		{
			result = testDataService.getTestKlanten()
		}
		return result;
	}
	
	def tegoed()
	{
		int klantId = params.klantId as int
		Klant klant = Klant.get(klantId)
		klant.laatsteToegang = new Date()
		
		def tegoed = klantService.tegoed(klant)
		def geblokkeerd = klantService.geblokkeerd(klant, tegoed)
		def result = [tegoed: tegoed, geblokkeerd: geblokkeerd]
		render "${params.callback}(${result as JSON})"
	}
}
