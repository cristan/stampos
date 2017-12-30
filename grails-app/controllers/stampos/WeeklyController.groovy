package stampos

class WeeklyController {

    def index()
	{
		def besteld = BestelRegel.executeQuery("select br.productPrijs.product.naam, sum(br.aantal), sum(br.aantal * br.productPrijs.prijs) from BestelRegel br group by br.productPrijs.product.naam")
		boolean noData = besteld.isEmpty()
		int totalAmount = 0
		BigDecimal totalRevenue = 0
		for(b in besteld) {
			totalAmount += b[1]
			totalRevenue += b[2]
		}
		return [noData: noData, besteld: besteld, totalAmount: totalAmount, totalRevenue: totalRevenue]
	}
}
