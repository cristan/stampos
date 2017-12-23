package stampos

import grails.converters.JSON

class BestellingController {

    static scaffold = true
	
	def klantService
	def generalService
	def pushService
	
	def bevestigBestelling()
	{
		def message = null;
		def klant = Klant.get(params.klantId as int);
		def tegoed = klantService.tegoed(klant)
		def geblokkeerd = klantService.geblokkeerd(klant, tegoed)
		
		if(!generalService.isAllowedToOrder(request))
		{
			message = "Bestelling niet verwerkt: bestellen is niet toegestaan"
		} 
		else if(geblokkeerd) 
		{
			message = "Bestelling niet verwerkt: je bent geblokkeerd."
		} 
		else 
		{
			def order = JSON.parse(params.order);
			Bestelling bestelling = new Bestelling(klant: klant)
			bestelling.save();
			def bestelRegels = []
			
			for(i in order)
			{
				def aantal = i[1];
				def pp = ProductPrijs.get(i[0].id);
				BestelRegel br = new BestelRegel(bestelling: bestelling, aantal: aantal, productPrijs: pp);
				br.save();
				bestelRegels.add(br)
			}
			bestelling.bestelRegels = bestelRegels
			pushService.orderDone(bestelling)
		}
		
		def result = [tegoed: tegoed, geblokkeerd: geblokkeerd, message:message]
		render "${result as JSON}"
	}
}
