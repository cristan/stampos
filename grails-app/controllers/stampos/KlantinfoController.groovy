package stampos

import java.text.DateFormat;
import java.text.DecimalFormat
import java.text.NumberFormat;
import java.text.SimpleDateFormat
import grails.util.Environment

import grails.converters.JSON

class KlantinfoController {

	private static final maxItems = 15;
	static DateFormat bestellingFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm")
	static DateFormat betalingFormat = new SimpleDateFormat("dd-MM-yyyy")
	static NumberFormat numberFormat = NumberFormat.getInstance(Locale.FRANCE)
	def testDataService
	
    def klantInfo() 
	{ 
		return [id: params.id]
	}
	
	def mutaties()
	{
		if(Environment.current == Environment.DEVELOPMENT)
		{
			testDataService.getTestBestellingen()
		}
		Klant klant = Klant.get(params.klantId)
		Date beginDatum = params.beginDatum ? new Date(params.beginDatum as long) : null
		
		String query = "from Bestelling b where b.klant = :klant"
		def namedParams = [klant:klant]
		if(beginDatum)
		{
			query += " and b.datum < :eersteBestellingDatum"
			namedParams.put("eersteBestellingDatum", beginDatum)
		}
		query += " order by b.datum desc"
		List<Bestelling> besteld = Bestelling.findAll (query, namedParams, [max: maxItems + 1])
		boolean nogOverigeBestellingen = besteld.size() > maxItems
		if(nogOverigeBestellingen)
		{
			// Deze (en daarop volgenden) worden de volgende keer geladen
			besteld.remove(besteld.size() - 1)
		}
		
		
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		
		def items = [];
		for(Bestelling bestelling: besteld)
		{
			def datumFormatted = bestellingFormat.format(bestelling.datum);
			def bestelregels = [];
			
			List<BestelRegel> sorted = new ArrayList<BestelRegel>()
			sorted.addAll(bestelling.bestelRegels)
			sorted.sort{it.productPrijs.product.naam}
			BigDecimal totaalBestelling = 0
			for(BestelRegel br : sorted)
			{
				BigDecimal totaal = br.aantal * br.productPrijs.prijs
				totaalBestelling += totaal
				bestelregels.add([aantal: br.aantal, product: br.productPrijs.product.naam, totaalPrijs : df.format(totaal)]);
			}
			
			items.add([datum:bestelling.datum, bestelling:[bestelregels: bestelregels, totaalBestelling: df.format(totaalBestelling), datumFormatted: bestellingFormat.format(bestelling.datum)]])
		}
		
		if(Environment.current == Environment.DEVELOPMENT)
		{
			testDataService.getTestBetalingen()
		}
		query = "from Betaling b where b.klant = :klant"
		def queryEnd = " order by b.datum desc"
		List<Betaling> betaald
		
		boolean nogOverigeBetalingen = false;
		if(nogOverigeBestellingen)
		{
			// De bestellingen zijn genoeg om maxItems te halen. Haal alle bestellingen die tussen de opgehaalde bestellingen vallen
			query += " and b.datum > :laatsteBestellingDatum" 
			namedParams = [klant:klant, laatsteBestellingDatum: besteld.get(maxItems -1).datum]
			if(beginDatum)
			{
				query += " and b.datum < :eersteBestellingDatum"
				namedParams.put("eersteBestellingDatum", beginDatum)
			}
			query += queryEnd
			betaald = Betaling.findAll (query, namedParams)
		}
		else
		{
			// Er zijn niet genoeg bestellingen om maxItems te halen. Daarom geen max aan het einde van de datum
			namedParams = [klant:klant]
			if(beginDatum)
			{
				query += " and b.datum < :eersteBestellingDatum"
				namedParams.put("eersteBestellingDatum", beginDatum)
			}
			query += queryEnd
			def maxBestellingen = maxItems - besteld.size() + 1
			def queryParams = [max: maxBestellingen]
			betaald = Betaling.findAll (query, namedParams, queryParams)
			nogOverigeBetalingen = betaald.size() == maxBestellingen
			if(nogOverigeBetalingen)
			{
				// Deze (en daarop volgenden) worden de volgende keer geladen
				betaald.remove(betaald.size() - 1)
			}
		}
		for(Betaling betaling : betaald)
		{
			items.add([datum:betaling.datum, betaling:[bedrag: df.format(betaling.bedrag), datumFormatted: betalingFormat.format(betaling.datum)]])
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
