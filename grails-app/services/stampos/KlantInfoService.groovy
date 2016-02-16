package stampos

import grails.transaction.Transactional

import java.text.DateFormat;
import java.text.SimpleDateFormat

@Transactional
class KlantInfoService {
	private static DateFormat bestellingFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm")
	private static DateFormat betalingFormat = new SimpleDateFormat("dd-MM-yyyy")
	
    def getJsonOrder(Bestelling bestelling, boolean addCustomerName) {
		def bestelregels = [];
		
		List<BestelRegel> sorted = new ArrayList<BestelRegel>()
		sorted.addAll(bestelling.bestelRegels)
		sorted.sort{it.productPrijs.product.naam}
		BigDecimal totaalBestelling = 0
		for(BestelRegel br : sorted)
		{
			BigDecimal totaal = br.aantal * br.productPrijs.prijs
			totaalBestelling += totaal
			bestelregels.add([aantal: br.aantal, product: br.productPrijs.product.naam, totaalPrijs : NumberUtils.formatNumber(totaal)]);
		}
		
		def item = [datum:bestelling.datum,
			bestelling:[
				bestelregels: bestelregels,
				totaalBestelling: NumberUtils.formatNumber(totaalBestelling),
				datumFormatted: bestellingFormat.format(bestelling.datum)]];
		if(addCustomerName)
		{
			item.put("klantnaam", bestelling.klant.naam)
		}
		return item
    }
	
	def getJsonPayment(Betaling betaling, boolean addCustomerName)
	{
		def item = [datum:betaling.datum, 
			betaling:[
				bedrag: NumberUtils.formatNumber(betaling.bedrag), 
				datumFormatted: betalingFormat.format(betaling.datum)]]
		if(addCustomerName)
		{
			item.put("klantnaam", betaling.klant.naam)
		}
		return item
	}
}
