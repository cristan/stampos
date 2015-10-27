package stampos

import java.text.DecimalFormat;
import java.text.NumberFormat;

class Product implements Comparable<Product> {
	def productPriceService
	
	String naam;
	int sortering;
	int colliGrootte;
	boolean zichtbaar;
	
	static hasMany = [productPrijzen: ProductPrijs]
	
    static mapping = {
		sort 'sortering','naam':'asc'
	}
	
    static constraints = {
		naam unique: true;
		colliGrootte min:1 ;
    }
	
	static transients = [ "price" ]
	
	@Override
	public String toString() {
		return naam
	}
	
	public String getPrice()
	{
		if(!id || !this)
		{
			return null
		}
		else
		{
			ProductPrijs activeProductPrice = productPriceService.getActiveProductPrice(this)
			if(activeProductPrice)
			{
				NumberFormat nf = NumberFormat.getInstance()
				nf.setMinimumFractionDigits(2)
				nf.setMaximumFractionDigits(2)
				return nf.format(activeProductPrice.prijs)
			}
			else
			{
				return null
			}
		}
	}
	
	public void setPrice(String price)
	{
		productPriceService.setPrice(this, NumberFormat.getInstance().parse(price))
	}

	@Override
	public int compareTo(Product other) {
		int sorteringCompare = sortering <=> other.sortering
       return sorteringCompare != 0 ? sorteringCompare : naam <=> other.naam
	}
}
