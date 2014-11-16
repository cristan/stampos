package stampos

import java.text.DateFormat;
import java.text.SimpleDateFormat

import groovy.transform.ToString;

class ProductPrijs {
	static DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
	
	Product product;
	BigDecimal prijs;
	Date actiefTot;

    static constraints = {
		actiefTot(nullable:true);
    }
	
	@Override
	public String toString() {
		String toReturn = product.naam +" voor \u20AC"+ prijs
		if(actiefTot == null)
		{
			toReturn += " (actief)"
		}
		else
		{
			toReturn += " (actief tot "+ format.format(actiefTot) +" )"
		}
	}
}
