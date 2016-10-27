package stampos

import java.text.DateFormat;
import java.text.SimpleDateFormat
import grails.util.Environment

import grails.converters.JSON

class KlantinfoController {

	private static final maxItems = 15;
	static DateFormat bestellingFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm")
	static DateFormat betalingFormat = new SimpleDateFormat("dd-MM-yyyy")
	def testDataService
	def klantInfoService
	
    def klantInfo() 
	{ 
		def zeId = params.id == "all" ? "null" : params.id 
		return [id: zeId]
	}
	
	def mutaties()
	{
		if(Environment.current == Environment.DEVELOPMENT)
		{
			testDataService.getTestBestellingen()
		}
		Klant klant = null
		if(params.klantId && params.klantId != "null")
		{
			klant = Klant.get(params.klantId)
		}
		Date beginDatum = params.beginDatum ? new Date(params.beginDatum as long) : null
		
		List<Bestelling> besteld;
		def queryParams = [max: maxItems + 1, sort:"datum"]
		if(klant) 
		{
			if(beginDatum) 
			{
				besteld = Bestelling.findAllByKlantAndDatumLessThan(klant, beginDatum, queryParams)
			}
			else 
			{
				besteld = Bestelling.findAllByKlant(klant, queryParams)
			}
		} 
		else 
		{
			if(beginDatum) 
			{
				besteld = Bestelling.findAllByDatumLessThan(beginDatum, queryParams)
			}
			else 
			{
				besteld = Bestelling.findAll(queryParams)
			}
		}
		 
		boolean nogOverigeBestellingen = besteld.size() > maxItems
		if(nogOverigeBestellingen)
		{
			// Deze (en daarop volgenden) worden de volgende keer geladen
			besteld.remove(besteld.size() - 1)
		}
		
		def items = [];
		for(Bestelling bestelling: besteld)
		{
			items.add(klantInfoService.getJsonOrder(bestelling, klant == null))
		}
		
		
		// Betalingen
		if(Environment.current == Environment.DEVELOPMENT)
		{
			testDataService.getTestBetalingen()
		}

		List<Betaling> betaald		
		boolean nogOverigeBetalingen = false;
		queryParams = [max: maxItems + 1, sort:"datum", order: "desc"]
		if(nogOverigeBestellingen)
		{
			// De bestellingen zijn genoeg om maxItems te halen. Haal alle bestellingen die tussen de opgehaalde bestellingen vallen
			
			def laatsteBestellingDatum = besteld.get(maxItems -1).datum
			if(beginDatum) {
				betaald = Betaling.findAllByDatumBetween(beginDatum, laatsteBestellingDatum, queryParams)
			} else {
				betaald = Betaling.findAllByDatumLessThan(laatsteBestellingDatum, queryParams)
			}
		}
		else
		{
			// Er zijn niet genoeg bestellingen om maxItems te halen. Daarom geen max aan het einde van de datum
			def maxBestellingen = maxItems - besteld.size() + 1
			queryParams.max = maxBestellingen
			if(beginDatum) {
				betaald = Betaling.findAllByDatumLessThan(beginDatum, queryParams)
			} else {
				println "all:"
				println Betaling.findAll()
				println "some:"
				betaald = Betaling.findAll(max: maxBestellingen, sort:"datum", order: "desc"){}//
				println betaald
			}

			nogOverigeBetalingen = betaald.size() == maxBestellingen
			if(nogOverigeBetalingen)
			{
				// Deze (en daarop volgenden) worden de volgende keer geladen
				betaald.remove(betaald.size() - 1)
			}
		}
		
		for(Betaling betaling : betaald)
		{
			items.add(klantInfoService.getJsonPayment(betaling, klant == null))
		}
		
		items.sort{-it.datum.getTime()}
		
		Long eindeDatum = null
		if(nogOverigeBestellingen || nogOverigeBetalingen)
		{
			eindeDatum = items.get(items.size() - 1).datum.getTime()
		}
		
		def toReturn = [eindeDatum: eindeDatum, items:items]
		render "${params.callback}(${toReturn as JSON})"
	}
}
