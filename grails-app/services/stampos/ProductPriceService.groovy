package stampos

import grails.transaction.Transactional

@Transactional
class ProductPriceService {

    ProductPrijs getActiveProductPrice(Product product) {
		def pps = ProductPrijs.findAllWhere(actiefTot: null, product: product)
		if(pps.size() == 0)
		{
			return null
		}
		else if (pps.size() > 1)
		{
			throw new Exception("Product "+ product.naam +" heeft meer dan 1 actieve ProductPrijs!")
		}
		else
		{
			return pps.get(0)
		}
    }
	
	def setPrice(Product product, BigDecimal price)
	{
		ProductPrijs pp = getActiveProductPrice(product)
		if(!pp)
		{
			// No ProductPrice exists: create a new one
			new ProductPrijs(product: product, prijs: price, actiefTot: null).save()
		}
		else
		{
			List<BestelRegel> orders = BestelRegel.findAllWhere(productPrijs: pp)
			if(!orders)
			{
				// when no orders exist, we can safely change the price
				pp.prijs = price
				pp.save()
			}
			else
			{
				// If orders already exist, we can't just change the price or else we'll retroactively change the balance
				// of the customers who have ordered this product.
				// Therefore, we'll set the end date of the active ProductPrice to now and create a new ProductPrice for the new price
				pp.actiefTot = new Date()
				pp.save()
				
				new ProductPrijs(product: product, prijs: price, actiefTot: null).save()
			}
		}
	}
	
}
