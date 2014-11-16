package stampos

class TestDataService {
	
	def grailsApplication

	def List<Klant> getTestKlanten()
	{
		List<Klant> result = Klant.findAllByZichtbaar(true, [sort:"naam"]);
		if(!result)
		{
			String[] klantNames = ["Daan Berkouwer", "Jeroen Berkouwer", "Anouk Bos", "Erik Bos", "Cyrano Coomans", "Axel de Jong", "Pepijn de Jong", "Roald de Jong",
				"Mike de Winter", "Sarah Deaney", "Dihanne Dijkman", "Don Diego du Clou", "Lelio Du Clou", "Anouk Faas", "Irene Faas", "Mart Gahler", "Yvonne Gibbon van Arnhem",
				"Chris Heemskerk", "Erik Heemskerk", "Tim Hoek", "Leonie Kah", "Hein Kluiver", "Hans Kouwenberg", "Maarten Kuijpers", "Erik Kuster", "Pieter Lok", "Cristan Meijer",
				"Elaine Molenaar", "Eva Mudde", "Jelmer Mudde", "Chris Mul", "Linda Oostrom", "Lisette Ouweneel", "Nikels Post", "Michelle Rietveld", "Renate Slappendel", "Roelof Spijker",
				"Rutger Sterk", "Danick Tahapary", "Jon Tober", "Mark Tober", "Saskia van Arnhem", "Bart van Dam", "Anna van den Heuvel", "Arthur van der Molen", "Kristina van der Molen",
				"Jeroen van Heel", "Jeroen van Hees", "Niels van Krimpen", "Paul van Lange", "Roy van Rij", "Niels van Tilburg", "Joost van Zwieten", "Stefan Veenstra", "Daphne Verboom",
				"Jeroen Verboom", "Vincent Verboom", "Ivo Verhoef", "Menno Verhoef", "Kees Vos", "Arjan Vuik"];
	
	
			Date uitstelTot = null
			def initieelUitstel = grailsApplication.config.initieelUitstel
			if(initieelUitstel && initieelUitstel != 0)
			{
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DATE, initieelUitstel);
				calendar.getTime();
			}
			for(String klantName : klantNames)
			{
				Klant klant = new Klant(naam: klantName, zichtbaar: true, uitstelTot: uitstelTot);
				klant.save();
			}
			result = Klant.findAllByZichtbaar(true, [sort:"naam"]);
		}

		return result
	}

	def getTestBestellingen()
	{
		getTestKlanten()
		Klant klant = Klant.findByNaam("Cristan Meijer");

		List<Bestelling> bestellingen = Bestelling.findAllWhere(klant: klant);
		if(!bestellingen.empty)
		{
			return bestellingen
		}

		Calendar c = Calendar.getInstance();
		List<Product> producten = getTestProducten()
		for(int i = 0; i < 31; i++)
		{
			c.add(Calendar.DATE, -1);
			Bestelling bestelling = new Bestelling(klant: klant, datum: c.getTime(), bestelRegels: [])
			bestelling.save()
			
			int aantalBestelRegels = ((int)Math.random() * Product.count())+1;
			def previousProducts = [];
			for(int j = 0; j < aantalBestelRegels; j++)
			{
				int aantal = ((int)Math.random() * 20)+1;
				def randomProduct;
				while(true)
				{
					randomProduct = producten.get((int)(Math.random() * producten.size()))
					if(!previousProducts.contains(randomProduct))
					{
						previousProducts.add(randomProduct);
						break;
					}
				}
				def pps = ProductPrijs.findAllWhere(actiefTot: null, product: randomProduct)
				
				BestelRegel br = new BestelRegel(bestelling: bestelling, aantal: aantal, productPrijs: pps.get(0));
				br.save();
				bestelling.bestelRegels.add(br)
			}
			bestelling.save();
		}
		
		return Bestelling.findAllWhere(klant: klant);
	}
	
	def getTestBetalingen()
	{
		getTestKlanten()
		Klant klant = Klant.findByNaam("Cristan Meijer");

		List<Betaling> betalingen = Betaling.findAllWhere(klant: klant);
		if(!betalingen.empty)
		{
			return betalingen
		}
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR, -4);
		for(int i = 0; i < 31; i++)
		{
			int aantalDagenTerug = ((int)Math.random() * 5) + 1
			c.add(Calendar.DATE, -aantalDagenTerug);
			BigDecimal bedrag = new BigDecimal(((int)Math.random() * 200)+1)
			Betaling betaling = new Betaling(klant: klant, datum: c.getTime(), bedrag: bedrag)
			betaling.save()
		}
		
		return Betaling.findAllWhere(klant: klant);
	}
	
	def getTestProducten()
	{
		List<Product> allProducts = Product.findAllWhere(zichtbaar: true);
		if(allProducts.isEmpty())
		{
			String[] productNames = ["Fris", "Chips", "Chocola", "Pils", "Duvel", "Rochefort 6", "La Trappe"];
			for(String productName : productNames)
			{
				Product product = new Product(naam: productName, colliGrootte: 24, zichtbaar:true);
				product.save();
			}
			
			allProducts = Product.findAllWhere(zichtbaar: true);
		}
		
		if(ProductPrijs.count == 0)
		{
			for(Product product : allProducts)
			{
				ProductPrijs pp = new ProductPrijs(product: product, prijs: 2.75)
				pp.save()
			}
		}
		return allProducts;
	}
}
