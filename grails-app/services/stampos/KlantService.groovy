package stampos

class KlantService {
	
	def grailsApplication

    def tegoed(Klant klant)
	{
		BigDecimal totaalBetaald = Betaling.executeQuery("select sum(bet.bedrag) from Betaling bet where bet.klant = ?", [klant])[0]
		BigDecimal totaalBesteld = ProductPrijs.executeQuery("select sum(pp.prijs * br.aantal) from ProductPrijs pp, BestelRegel br, Bestelling bes where br.bestelling = bes and br.productPrijs = pp and bes.klant = ?", [klant])[0]
		if(totaalBetaald == null)
		{
			totaalBetaald = 0
		}
		if(totaalBesteld == null)
		{
			totaalBesteld = 0
		}
		
		return totaalBetaald - totaalBesteld
	}
	
	def geblokkeerd(Klant klant)
	{
		return geblokkeerd(klant, null);
	}
	
	def laatstBetaald(Klant klant)
	{
		def betalingen = Betaling.where{klant == klant}.list(sort: 'datum', order: 'desc', max: 1)
		if(!betalingen)
		{
			return null
		}
		else
		{
			println "datum: "+ betalingen[0].datum
			return betalingen[0].datum
		}
	}
	
	def geblokkeerd(Klant klant, BigDecimal tegoedKlant)
	{
		if(klant.uitstelTot)
		{
			Calendar calendar = Calendar.getInstance()
			calendar.add(Calendar.DATE, -1)
			Date yesterday = calendar.getTime();
			if(yesterday.before(klant.uitstelTot))
			{
				return false;
			}
		}
		
		if(tegoedKlant == null)
		{
			// We hadden niet nog ergens een tegoed die we konden recyclen
			tegoedKlant = tegoed(klant)
		}
		if(tegoedKlant >= 0)
		{
			return false
		}
		else
		{
			Date evenGeleden = new Date(System.currentTimeMillis() - grailsApplication.config.geblokkeerdPeriode)
			BigDecimal besteldAfgelopenTijd = ProductPrijs.executeQuery("select sum(pp.prijs * br.aantal) from ProductPrijs pp, BestelRegel br, Bestelling bes where bes.klant = ? and bes.datum > ?", [klant, evenGeleden])[0]
			if(besteldAfgelopenTijd)
			{
				return tegoedKlant + besteldAfgelopenTijd < 0
			}
			return true
		}
	}
	
	def komendeMaandag()
	{
		Calendar calendar = Calendar.instance
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
		calendar.set(Calendar.HOUR_OF_DAY, 23)
		calendar.set(Calendar.MINUTE, 59)
		calendar.set(Calendar.SECOND, 59)
		
		if(calendar < Calendar.instance)
		{
			calendar.add(Calendar.WEEK_OF_YEAR, 1);
		}
		
		return calendar.getTime()
	}
}
