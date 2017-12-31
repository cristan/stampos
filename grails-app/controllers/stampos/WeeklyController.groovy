package stampos

import java.text.DateFormat
import java.text.SimpleDateFormat

class WeeklyController {
	
	static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy")

	def index() {
		Date today = new Date().clearTime()
		Date endDate = params.endDate ? new Date(params.endDate as long) : today
		Date beginDate = params.beginDate ? new Date(params.beginDate as long) : endDate - 7

		def ordered = BestelRegel.executeQuery("select br.productPrijs.product.naam, sum(br.aantal), sum(br.aantal * br.productPrijs.prijs) from BestelRegel br "+
			"where br.bestelling.datum > :dateFrom and br.bestelling.datum < :dateTo "+
			"group by br.productPrijs.product.naam "+
			"order by br.productPrijs.product.sortering, br.productPrijs.product.naam",
				[dateFrom: beginDate, dateTo: endDate])
		boolean nothingOrdered = ordered.isEmpty()
		int totalAmount = 0
		BigDecimal totalRevenue = 0
		for(o in ordered) {
			totalAmount += o[1]
			totalRevenue += o[2]
		}

		def paid = Betaling.findAllByDatumGreaterThanAndDatumLessThan(beginDate, endDate)

		def nothingPaid = paid.isEmpty()
		BigDecimal totalPaid = 0
		for(p in paid) {
			totalPaid += p.bedrag
		}
		
		return [
			nothingOrdered: nothingOrdered,
			ordered: ordered,
			totalAmount: totalAmount,
			totalRevenue: totalRevenue, 
			nothingPaid: nothingPaid, 
			totalPaid: totalPaid, 
			paid: paid, 
			beginDateFormatted: dateFormat.format(beginDate),
			endDateFormatted: dateFormat.format(endDate),
			previousBeginDate: (beginDate - 7).getTime(), 
			previousEndDate: (endDate - 7).getTime(),
			nextBeginDate: (beginDate + 7).getTime(), 
			nextEndDate: (endDate + 7).getTime(),
			shouldShowLinkToNextWeek: endDate < today]
	}
}
