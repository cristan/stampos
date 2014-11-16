package stampos

import java.text.DateFormat;
import java.text.SimpleDateFormat

class Betaling {
	static DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
	
	static belongsTo = [klant: Klant]
	BigDecimal bedrag
	Date datum = new Date()

    static constraints = {
		//bedrag(min:0.01)
    }
	
	@Override
	public String toString() {
		if(!klant)
		{
			return "Een nieuwe Betaling"
		}
		else
		{
			return "\u20AC"+bedrag +" betaald door "+ klant.naam +" op "+ format.format(datum)
		}
	}
}
