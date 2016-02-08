package stampos

import grails.converters.JSON

class BestellingController {

    static scaffold = true
	
	def klantService
	def generalService
	def pushService
	
	def bevestigBestelling()
	{
		def message = "";
		def klant = Klant.get(params.klantId as int);
		if(!generalService.isAllowedToOrder(request))
		{
			message = "Bestellen mag niet, dus je bestelling is dan ook niet doorgegaan."
		}
		else
		{
			def order = JSON.parse(params.order);
			Bestelling bestelling = new Bestelling(klant: klant)
			bestelling.save();
			
			for(i in order)
			{
				def aantal = i[1];
				def pp = ProductPrijs.get(i[0].id);
				BestelRegel br = new BestelRegel(bestelling: bestelling, aantal: aantal, productPrijs: pp);
				br.save();
			}
			pushService.orderDone(bestelling)
		}
		
		
		def tegoed = klantService.tegoed(klant)
		def geblokkeerd = klantService.geblokkeerd(klant, tegoed)
		def result = [tegoed: tegoed, geblokkeerd: geblokkeerd, message:message]
		render "${result as JSON}"
	}
}
