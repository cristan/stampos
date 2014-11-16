package stampos

import grails.converters.JSON

class ProductController {

	static scaffold = true
	def testDataService
	
	def all()
	{
		def pps = getAllProductPrijzen();
		def result = [];
		for(ProductPrijs pp : pps)
		{
			result.add([id: pp.id, naam:pp.product.naam, prijs:pp.prijs])
		}
		
		render "${params.callback}(${result as JSON})"
	}
	
	def private getAllProductPrijzen()
	{
		List<Product> allProducts = Product.findAllWhere(zichtbaar: true);
		if(allProducts.isEmpty())
		{
			allProducts = testDataService.getTestProducten()
		}
		//Dubbelop sorteren omdat de sort in de mapping van Product wordt genegeerd met findAllWhere
		allProducts.sort();
		
		def result = [];
		for(Product product : allProducts)
		{
			def pps = ProductPrijs.findAllWhere(actiefTot: null, product: product)
			if(pps.size() == 0)
			{
				throw new Exception("Geen actieve ProductPrijs gevonden van product: "+ product +". "
					+"Voeg een ProductPrijs toe, maak het product onzichtbaar of verwijder het product")
			}
			else if (pps.size() > 1)
			{
				throw new Exception("Product "+ product.naam +" heeft meer dan 1 actieve ProductPrijs!")
			}
			else
			{
				result.add(pps.get(0))
			}
		}
		
		return result;
	}
}
