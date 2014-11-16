package stampos

class BestelRegel {

	static belongsTo = [bestelling: Bestelling]
	int aantal
	ProductPrijs productPrijs
	
    static constraints = {
		aantal(min:1)
    }
	
	@Override
	public String toString() {
		return aantal +" "+ productPrijs.product.naam +" a "+ productPrijs.prijs +" (totaal: \u20AC"+ aantal * productPrijs.prijs +")"
	}
}
