package stampos

import java.text.DateFormat
import java.text.SimpleDateFormat;

class Bestelling {
	
	static DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
	
	static belongsTo = [klant: Klant]
	Date datum = new Date()
	static hasMany = [bestelRegels: BestelRegel]

    static constraints = {
    }
	
	@Override
	public String toString() {
		return "Bestelling van "+ klant.naam +" op "+ format.format(datum)
	}
}
