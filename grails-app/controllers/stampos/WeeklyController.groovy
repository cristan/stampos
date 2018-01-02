package stampos

import java.text.DateFormat
import java.text.SimpleDateFormat

class WeeklyController {
	
	static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy")

	def index() {
		Date now = new Date()
		Date beginDate = params.beginDate ? new Date(params.beginDate as long) : getStartOfWeek(now)
		Date endDate = params.endDate ? new Date(params.endDate as long) : getEndOfWeek(now)
		
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
			shouldShowLinkToNextWeek: endDate < getEndOfWeek(now)]
	}
	
	private Date getStartOfWeek(Date date) {
		def cal = Calendar.instance
		cal.setTime(date);
		
		while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
			cal.add(Calendar.DAY_OF_WEEK, -1)
		}
		
		return getStartOfDay(cal.time)
	}
	
	private Date getEndOfWeek(Date date) {
		def cal = Calendar.instance
		cal.setTime(date);
		
		while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
			cal.add(Calendar.DAY_OF_WEEK, +1)
		}
		
		return getEndOfDay(cal.time)
	}
	
	public Date getEndOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}
	
	public Date getStartOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

}
