package stampos

import grails.converters.JSON

class TellingController {

    static scaffold = true
	
	def registreerTelling()
	{
		def producten = Product.list()
		return [producten: producten]
	}
	
	def verwerkTelling()
	{
		String start = "aantalKeerProduct"
		Telling telling = null
		def tellingRegels = []
		
		for(String param in params.keySet())
		{
			if(param.startsWith(start))
			{
				def productId = param.substring(start.length()) as int
				
				Product product = Product.get(productId as int);
				
				def aantal = params.get(start+productId)
				
				if(!telling)
				{
					telling = new Telling(datum: new Date())
					telling.save()
				}
				if(aantal)
				{
					TellingRegel tr = new TellingRegel(aantal: aantal as int, product: product, telling: telling)
					tr.save()
					tellingRegels.add(tr)
					
					log.debug(aantal +" x "+ product.naam)
				}
				
			}
		}
		
		return [tellingRegels:tellingRegels]
	}
}
