package stampos

class Inkoop {
	
	Date datum
	static hasMany = [inkoopRegels : InkoopRegel]

    static constraints = {
    }
}
