package stampos

class KlantRekeningnummer {

	static belongsTo = [klant: Klant]
	String rekening
	
    static constraints = {
		rekening(unique:true)
    }
}
