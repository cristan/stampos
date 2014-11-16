package stampos

class DedupliceerController {

    def index()
	{
		List<Bestelling> alleBestellingen = Bestelling.findAll ([sort:"datum"]);
		Set<Bestelling> huidigeProblemen = []
		def toReturn = []
		boolean combo = false;
		for(int i = 1; i < alleBestellingen.size()-1; i++)
		{
			Bestelling oud = alleBestellingen.get(i - 1)
			Bestelling nieuw = alleBestellingen.get(i)
			long timeDifference = nieuw.getDatum().getTime() - oud.getDatum().getTime()
			if(timeDifference < 5000 && orderedTheSame(oud, nieuw))
			{
				combo = true;
				huidigeProblemen.add(oud)
				huidigeProblemen.add(nieuw)
			}
			else if(combo == true)
			{
				//C-C-C-C-Combo breaker!
				combo = false
				List<Bestelling> gesorteerdeProblemen = []
				gesorteerdeProblemen.addAll(huidigeProblemen)
				gesorteerdeProblemen.sort({it.datum})
				huidigeProblemen.clear();
				toReturn.add(gesorteerdeProblemen)
			}
		}
		
		return [bestellingenSets: toReturn]
	}
	
	def orderedTheSame(Bestelling bestelling1, Bestelling bestelling2)
	{
		if(!hasEveryThing(bestelling1, bestelling2))
		{
			return false;
		}
		return hasEveryThing(bestelling2, bestelling1)
	}
	
	def hasEveryThing(Bestelling bestelling1, Bestelling bestelling2)
	{
		for(BestelRegel br : bestelling1.bestelRegels)
		{
			boolean found = false
			for(BestelRegel br2 : bestelling2.bestelRegels)
			{
				if(br.productPrijs.product == br2.productPrijs.product)
				{
					if(br.aantal == br2.aantal)
					{
						found = true;
						break;
					}
					else
					{
						return false;
					}
				}
			}
			if(!found)
			{
				return false;
			}
		}
		return true;
	}
}
