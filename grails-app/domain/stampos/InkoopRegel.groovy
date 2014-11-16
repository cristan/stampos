package stampos

import groovy.transform.ToString;

class InkoopRegel {
	int aantal
	Product product
	static belongsTo = [inkoop : Inkoop] 
	
    static constraints = {
    }
	
	@Override
	public String toString() {
		return aantal +" x "+ product.naam
	}
}
