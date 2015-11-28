package stampos

import grails.converters.JSON

class InkoopController {

    static scaffold = true
	
	def registreerInkopen()
	{
		def producten = Product.list()
		return [producten: producten, alleProductenAsJson: producten as JSON]
	}
	
	def verwerkInkopen()
	{
		String colliGrootteStart = "colliGrootte"
		Inkoop inkoop = null
		def inkoopRegels = []
		
		for(String param in params.keySet())
		{
			if(param.startsWith(colliGrootteStart))
			{
				def productId = param.substring(colliGrootteStart.length()) as int
				
				Product product = Product.get(productId as int);
				
				def colliGrootte = params.get(colliGrootteStart+productId) as int
				product.setColliGrootte(colliGrootte)
				
				def aantal = params.get("aantal"+ productId)
				if(aantal)
				{
					if(!inkoop)
					{
						inkoop = new Inkoop(datum: new Date())
						inkoop.save()
					}
					InkoopRegel ir = new InkoopRegel(aantal: aantal, product: product, inkoop: inkoop)
					ir.save()
					inkoopRegels.add(ir)
					
					println aantal +" x "+ product.naam
				}
			}
		}
		
		return [inkoopRegels:inkoopRegels]
	}
}
