package stampos

class Telling {
	Date datum
	static hasMany = [tellingRegels : TellingRegel]
	
    static constraints = {
    }
}
