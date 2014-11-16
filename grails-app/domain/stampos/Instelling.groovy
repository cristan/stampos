package stampos

class Instelling {
	String naam
	String waarde

    static constraints = {
		naam(unique:true)
    }
}
