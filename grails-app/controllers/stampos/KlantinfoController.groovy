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
			testDataService.getTestBetalingen()
		}
		
		Klant klant = null
		if(params.klantId && params.klantId != "null")
		{
			klant = Klant.get(params.klantId)
		}
		Date beginDatum = params.beginDatum ? new Date(params.beginDatum as long) : null
		
		// Bestellingen
		def besteld = getOrders(klant, beginDatum, maxItems + 1)
		 
		// Betalingen
		List<Betaling> betaald 
		if(besteld.size() >= maxItems) {
			// De bestellingen zijn genoeg om maxItems te halen. 
			// Daarom hebben we alleen alle betalingen nodig 
			// die tussen de eerste _maxItems_ bestellingen vallen
			def laatsteBestellingDatum = besteld.get(maxItems -1).datum
			betaald = getPayments(klant, beginDatum, laatsteBestellingDatum, maxItems + 1) 
		} else {
			betaald = getPayments(klant, beginDatum, maxItems + 1)
		} 
		
		// Build the JSON to return
		def items = [];
		for(Bestelling bestelling: besteld) {
			items.add(klantInfoService.getJsonOrder(bestelling, klant == null))
		}
		for(Betaling betaling : betaald) {
			items.add(klantInfoService.getJsonPayment(betaling, klant == null))
		}
		items.sort{-it.datum.getTime()}
		int excessItems = maxItems - items.size()
		Long eindeDatum = null
		if(excessItems > 0) {
			items = items.subList(0, maxItems - 1)
			eindeDatum = items.get(maxItems - 1).datum.getTime()
		}
		
		def toReturn = [eindeDatum: eindeDatum, items:items]
		render "${params.callback}(${toReturn as JSON})"
	}
	
	private List<Bestelling> getOrders(Klant klant, Date beginDatum, int maxNumberOfItems) {
		List<Bestelling> besteld;
		def queryParams = [max: maxNumberOfItems, sort:"datum"]
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
				besteld = Bestelling.findAll(queryParams){}// TODO: Doesn't work
			}
		}
	}

	private List<Betaling> getPayments(Klant klant, Date beginDatum, Date laatsteBestellingDatum, int numberOfPayments) {
		List<Betaling> betaald
		def queryParams = [max: numberOfPayments, sort:"datum", order: "desc"]
		
		if(klant) {
			if(beginDatum) {
				betaald = Betaling.findAllByKlantAndDatumBetween(klant, beginDatum, laatsteBestellingDatum, queryParams)
			} else {
				betaald = Betaling.findAllByKlantAndDatumLessThan(klant, laatsteBestellingDatum, queryParams)
			}
		} else {
			if(beginDatum) {
				betaald = Betaling.findAllByDatumBetween(beginDatum, laatsteBestellingDatum, queryParams)
			} else {
				betaald = Betaling.findAllByDatumLessThan(laatsteBestellingDatum, queryParams)
			}
		}
		
		return betaald
	}
	
	private List<Betaling> getPayments(Klant klant, Date beginDatum, int maxNumberOfItems) {
		List<Betaling> paid;
		def queryParams = [max: maxNumberOfItems, sort:"datum"]
		if(klant)
		{
			if(beginDatum)
			{
				paid = Betaling.findAllByKlantAndDatumLessThan(klant, beginDatum, queryParams)
			}
			else
			{
				paid = Betaling.findAllByKlant(klant, queryParams)
			}
		}
		else
		{
			if(beginDatum)
			{
				paid = Betaling.findAllByDatumLessThan(beginDatum, queryParams)
			}
			else
			{
				// TODO: Probably doesn't work
				paid = Betaling.findAll(queryParams)
			}
		}
	}
	
	
}
