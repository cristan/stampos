package stampos

import java.text.DateFormat;
import java.text.SimpleDateFormat

class RendementController {
	
	static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy")

    def schatting()
	{
		List<Inkoop> inkopen = Inkoop.all
		if(inkopen.size() < 2)
		{
			return [noData: true]
		}
		else
		{
			def periodes = []
			def totaalInkoopRegels = []
			for(int i = 1; i < inkopen.size(); i++)
			{
				Inkoop inkoop1 = inkopen.get(i - 1)
				Inkoop inkoop2 = inkopen.get(i)
				
				// In deze for loop de totaalInkoopRegels bijwerken om een totaaloverzicht te kunnen maken
				for(InkoopRegel ir : inkoop2.inkoopRegels)
				{
					boolean found = false
					for(def bestaandeIr : totaalInkoopRegels)
					{
						if(bestaandeIr.product.id == ir.product.id)
						{
							bestaandeIr.aantal += ir.aantal
							found = true
							break
						}
					}
					
					if(!found)
					{
						totaalInkoopRegels.add([aantal: ir.aantal, product: ir.product])
					}
				}
				
				// De echte magie: een periode toevoegen
				Date beginDate = inkoop1.datum
				Date endDate = inkoop2.datum
				def inkoopRegels = inkoop2.inkoopRegels
				periodes.add(getStatistieken(beginDate, endDate, inkoopRegels))
			}
			
			Date totalEndDate = inkopen.get(inkopen.size() - 1).datum
			def totaal = getStatistieken(inkopen.get(0).datum, totalEndDate, totaalInkoopRegels)
			return [noData: false, periodes: periodes, totaal: totaal]
		}
	}
	
	def geteld()
	{
		List<Telling> tellingen = Telling.all
		if(tellingen.size() < 2)
		{
			return [noData: true]
		}
		else
		{
			def periodes = []
			def tellingRegels = []
			for(int i = 1; i < tellingen.size(); i++)
			{
				Telling telling1 = tellingen.get(i - 1)
				Telling telling2 = tellingen.get(i)
				
				Date beginDate = telling1.datum
				Date endDate = telling2.datum
				
				// Ingekocht
				def ingekocht = InkoopRegel.executeQuery("select sum(ir.aantal), ir.product from InkoopRegel ir where ir.inkoop.datum > :date1 and ir.inkoop.datum < :date2 group by ir.product.id", [date1: beginDate, date2:endDate])
				
				def inkoopRegels = []
				
				for(def inkoop : ingekocht)
				{
					def aantal = inkoop[0]
					def product = inkoop[1]
					inkoopRegels.add([aantal: aantal, product: product])
				}
				
				// Er vanuit gaande dat we eindigen met een lege bar, is alle voorraad die we eerst hadden nu weg en is dus ook ingekocht...
				for(TellingRegel tellingRegel : telling1.tellingRegels)
				{
					def product = tellingRegel.product
					def aantal = tellingRegel.aantal
					boolean found = false
					for(def inkoopRegel : inkoopRegels)
					{
						if(inkoopRegel.product.id == product.id)
						{
							inkoopRegel.aantal += aantal
							found = true
							break
						}
					}
					
					if(!found)
					{
						inkoopRegels.add([aantal: aantal, product: product])
					}
				}
				
				// ... maar de koelkast was niet leeg, dus alles wat er nog in zat mag weer afgetrokken worden van wat er ingekocht is
				for(TellingRegel tellingRegel : telling2.tellingRegels)
				{
					def product = tellingRegel.product
					def aantal = tellingRegel.aantal
					boolean found = false
					for(def inkoopRegel : inkoopRegels)
					{
						if(inkoopRegel.product.id == product.id)
						{
							inkoopRegel.aantal -= aantal
							found = true
							break
						}
					}
					
					if(!found)
					{
						inkoopRegels.add([aantal: -aantal, product: product])
					}
				}
				
				
				periodes.add(getStatistieken(beginDate, endDate, inkoopRegels))
			}
			
			return [noData: false, periodes: periodes]
		}
	}
	
	private def getStatistieken(Date beginDate, Date endDate, def inkoopRegels)
	{
		def totaalIngekocht = 0
		def totaalBesteld = 0
		
		List<Product> producten = []
		
		// Ingekocht
		System.out.println("inkoopRegels: "+ inkoopRegels);
		for(def inkoopRegel : inkoopRegels)
		{
			totaalIngekocht += inkoopRegel.aantal
			if(!producten*.id.contains(inkoopRegel.product.id))
			{
				producten.add(inkoopRegel.product)
			}
		}
		
		
		// Besteld
		// I don't know why " group by br.productPrijs.product.id" is needed, but it is when you have ordered something which isn't bought or this in reverse 
		def besteld = BestelRegel.executeQuery("select sum(br.aantal), br.productPrijs.product from BestelRegel br where br.bestelling.datum > :date1 and br.bestelling.datum < :date2 group by br.productPrijs.product.id", [date1: beginDate, date2:endDate])
		System.out.println("besteld: "+ besteld);
		
		// Loop over wat er besteld is heen om totaal besteld te berekenen en de lijst met de producten te updaten
		// (besteld wordt later trouwens weer gebruikt)
		for(def bestelling : besteld)
		{
			def aantal = bestelling[0]
			totaalBesteld += aantal
			
			def product = bestelling[1]
			if(!producten*.id.contains(product.id))
			{
				producten.add(product)
			}
		}
		
		
		// Sorteer producten
		Collections.sort(producten, new Comparator<Product>()
			{
				int compare(Product p1,  Product p2)
				{
					return p1.naam.compareTo(p2.naam)
				};
			}
			)
		System.out.println("producten: "+ producten);
		
		
		// Lijst maken
		def rendementLijst = []
		for(Product product : producten)
		{
			def aantalIngekocht = 0;
			for(def inkoopRegel : inkoopRegels)
			{
				if(inkoopRegel.product.id == product.id)
				{
					aantalIngekocht = inkoopRegel.aantal
					break
				}
			}
			
			def aantalBesteld = 0
			for(def bestelling : besteld)
			{
				if(bestelling[1].id == product.id)
				{
					aantalBesteld = bestelling[0]
					break;
				}
			}
			
			def percentage = getPercentage(aantalIngekocht, aantalBesteld)
			
			rendementLijst.add([product:product, aantalIngekocht: aantalIngekocht, aantalBesteld:aantalBesteld, percentage: percentage])
		}
		
		def totalPercentage = getPercentage(totaalIngekocht, totaalBesteld)
		return [beginDate: dateFormat.format(beginDate), endDate: dateFormat.format(endDate), totaalIngekocht: totaalIngekocht, totaalBesteld: totaalBesteld, totalPercentage: totalPercentage, rendementLijst: rendementLijst]
	}
	
	private String getPercentage(def number1, def number2)
	{
		if(number1 == 0)
		{
			return "&infin;"
		}
		else
		{
			def percentage = (number2 / number1) * 100
			return Math.round(percentage) + "%"
		}
	}
}
