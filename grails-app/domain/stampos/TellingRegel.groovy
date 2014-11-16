package stampos

class TellingRegel {
	int aantal
	Product product
	static belongsTo = [telling: Telling]

    static constraints = {
    }
	
	@Override
	public String toString() {
		return aantal +" x "+ product.naam
	}
}
