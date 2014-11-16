package stampos

import groovy.transform.ToString;

class Klant {

	String naam;
	String email;
	String wachtwoord;
	Date uitstelTot;
	boolean zichtbaar;
	static hasMany = [betalingen: Betaling, bestellingen: Bestelling]
	
	static mapping = {
	    sort naam: "asc"
		
		betalingen cascade: "all-delete-orphan"
	}
	
    static constraints = {
		naam unique: true;
		email nullable:true 
		wachtwoord nullable:true 
		uitstelTot nullable:true 
    }
	
	@Override
	public String toString() {
		return naam;
	}
}
