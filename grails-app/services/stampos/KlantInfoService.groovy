package stampos

import grails.transaction.Transactional

import java.text.DateFormat;
import java.text.DecimalFormat
import java.text.SimpleDateFormat

@Transactional
class KlantInfoService {
	private static DateFormat bestellingFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm")
	private static DateFormat betalingFormat = new SimpleDateFormat("dd-MM-yyyy")
	private DecimalFormat moneyFormat;
	
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
			bestelregels.add([aantal: br.aantal, product: br.productPrijs.product.naam, totaalPrijs : getMoneyFormat().format(totaal)]);
		}
		
		def item = [datum:bestelling.datum,
			bestelling:[
				bestelregels: bestelregels,
				totaalBestelling: getMoneyFormat().format(totaalBestelling),
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
				bedrag: getMoneyFormat().format(betaling.bedrag), 
				datumFormatted: betalingFormat.format(betaling.datum)]]
		if(addCustomerName)
		{
			item.put("klantnaam", betaling.klant.naam)
		}
		return item
	}
	
	private DecimalFormat getMoneyFormat()
	{
		if(moneyFormat == null)
		{
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			df.setMinimumFractionDigits(2);
			moneyFormat = df;
		}
		return moneyFormat
	}
}
