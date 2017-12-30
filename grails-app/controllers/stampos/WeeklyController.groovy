package stampos

class WeeklyController {

    def index()
	{
		def now = new Date()
		def aWeekAgo = now - 7
		
		def ordered = BestelRegel.executeQuery("select br.productPrijs.product.naam, sum(br.aantal), sum(br.aantal * br.productPrijs.prijs) from BestelRegel br where br.bestelling.datum > :dateFrom and br.bestelling.datum < :dateTo group by br.productPrijs.product.naam", 
			[dateFrom: aWeekAgo, dateTo: now])
		boolean nothingOrdered = ordered.isEmpty()
		int totalAmount = 0
		BigDecimal totalRevenue = 0
		for(o in ordered) {
			totalAmount += o[1]
			totalRevenue += o[2]
		}
		
		def paid = Betaling.findAllByDatumGreaterThanAndDatumLessThan(aWeekAgo, now)
		
		def nothingPaid = paid.isEmpty()
		BigDecimal totalPaid = 0
		for(p in paid) {
			totalPaid += p.bedrag
		}
		
		return [nothingOrdered: nothingOrdered, ordered: ordered, totalAmount: totalAmount, totalRevenue: totalRevenue, nothingPaid: nothingPaid, totalPaid: totalPaid, paid: paid]
	}
}
