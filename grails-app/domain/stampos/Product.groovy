package stampos

class Product implements Comparable<Product> {

	String naam;
	int sortering;
	int colliGrootte;
	boolean zichtbaar;
	
	static hasMany = [productPrijzen: ProductPrijs]
	
    static mapping = {
		sort 'sortering','naam':'asc'
	}
	
    static constraints = {
		naam unique: true;
		colliGrootte min:1 ;
    }
	
	@Override
	public String toString() {
		return naam
	}

	@Override
	public int compareTo(Product other) {
		int sorteringCompare = sortering <=> other.sortering
       return sorteringCompare != 0 ? sorteringCompare : naam <=> other.naam
	}
}
