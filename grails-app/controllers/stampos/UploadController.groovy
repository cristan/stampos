package stampos

import grails.converters.JSON

import java.text.DateFormat;
import java.text.NumberFormat
import java.text.SimpleDateFormat

class UploadController {

	public static final String INITIAL_DATE = "eersteDatum"
	private static final String AMOUNT_FIRST_DATE = "hoeveelheidEersteDatum"

    def upload() { }
	
	SimpleDateFormat backingDateFormat = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat sensibleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	def dontUppercaseFirstLetter = ["DE", "VAN", "HET", "EEN", "EN"]
	String transactionDateIdentifier = "transactiedatum:"
	
	def doUpload() {
		def file = request.getFile('file')
		def allLines = file.inputStream.toCsvReader().readAll()
		
		def allTransactions = [];
		
		def firstDatum = null
		def datumOccurences = 0;
		
		for(def line in allLines)
		{
			//"Datum","Naam / Omschrijving","Rekening","Tegenrekening","Code","Af Bij","Bedrag (EUR)","MutatieSoort","Mededelingen"
			String afBij = line[5]
			String mutatieSoort = line[7]
			
			if(afBij.equals("Bij"))
			{
				// Verander de datum van 20130520 naar 20-05-2013. 
				// reden 1: de datum wordt letterlijk vertoond in doUpload.gsp
				// reden 2: de datum werd opgeslagen in dit formaat als de instelling eersteDatum, en om dit niet te breken blijven we dit doen
				String datum = line[0]
				Date datumDate = backingDateFormat.parse(datum);
				datum = sensibleDateFormat.format(datumDate);
				
				String echteNaamOmschrijving = line[1]
				String naamOmschrijving = fixCase(echteNaamOmschrijving)
				String tegenrekening = line[3]
				String bedrag = line[6]
				String echteMededelingen = line[8]
				
				String mededelingen = sanitizeDescription(echteMededelingen, echteNaamOmschrijving)
				
				String gevondenKlantNaam = null
				KlantRekeningnummer kr = KlantRekeningnummer.findByRekening(tegenrekening)
				if(kr)
				{
					gevondenKlantNaam = kr.klant.naam
				}
				
				if(!firstDatum)
				{
					firstDatum = datum
					datumOccurences++
				}
				else if(firstDatum == datum)
				{
					datumOccurences++
				}
				
				//String code = line[4] // GT is mobile, OV is overmaak
				//String mutatieSoort = line[7] //Overschrijving, Internetbankieren
				//String rekening = line[2] // Als het goed is altijd hetzelfde
				
				allTransactions.add([datum: datum, echteNaamOmschrijving: echteNaamOmschrijving, naamOmschrijving: naamOmschrijving, tegenrekening: tegenrekening, bedrag: bedrag, mededelingen: mededelingen, echteMededelingen: echteMededelingen, gevondenKlantNaam: gevondenKlantNaam])
			}
		}
		
		allTransactions = allTransactions.reverse()
		def newTransactions = []
		def oldTransactions = []
		
		Instelling eersteDatumInstelling = Instelling.findWhere(naam : INITIAL_DATE)
		Instelling hoeveelheidEersteDatumInstelling = Instelling.findWhere(naam : AMOUNT_FIRST_DATE)
		def eersteDatumFound = 0
		
		if(eersteDatumInstelling && hoeveelheidEersteDatumInstelling)
		{
			String eersteDatumString = eersteDatumInstelling.waarde
			Date eersteDatum = sensibleDateFormat.parse(eersteDatumString)
			int hoeveelheidEersteDatum = hoeveelheidEersteDatumInstelling.waarde as int
			
			for(def transaction in allTransactions)
			{
				if(transaction.datum == eersteDatumString)
				{
					eersteDatumFound++
					if(eersteDatumFound > hoeveelheidEersteDatum)
					{
						newTransactions.add(transaction)
					}
					else
					{
						oldTransactions.add(transaction)
					}
				}
				else{
					Date datum = sensibleDateFormat.parse(transaction.datum)
					if(datum.before(eersteDatum))
					{
						oldTransactions.add(transaction)
					}
					else
					{
						newTransactions.add(transaction)
					}
				}
			}
		}
		else
		{
			// No previous import detected: show all
			newTransactions = allTransactions 
		}
		
		newTransactions = newTransactions.reverse()
		oldTransactions = oldTransactions.reverse()
		
		
		def klantnamen = []
		for(def klant in Klant.getAll())
		{
			klantnamen.add(klant.naam)
		}
		
		String customerNamesJSON = klantnamen as JSON
		return [eersteDatum: firstDatum, hoeveelheidEersteDatum: datumOccurences, newTransactions: newTransactions, oldTransactions: oldTransactions, klantnamen: customerNamesJSON]
	}
	
	def sanitizeDescription(String mededelingen, String naamOmschrijving)
	{
		def descriptionIdentifier = "Omschrijving: ";
		if(!mededelingen.contains(descriptionIdentifier))
		{
			return null
		}
		def weirdPostbankStart = "Naam: "+naamOmschrijving+descriptionIdentifier
		if(mededelingen.startsWith(weirdPostbankStart))
		{
			mededelingen = mededelingen.substring(weirdPostbankStart.length())
			int endIndex = mededelingen.indexOf("Kenmerk: ")
			if(endIndex == -1)
			{
				endIndex = mededelingen.indexOf("IBAN: ")
			}
			if(endIndex != -1)
			{
				mededelingen = mededelingen.substring(0, endIndex)
			}
		}
		
		return mededelingen.trim()
	}
	
	private def fixCase(String toFix)
	{
		StringTokenizer st = new StringTokenizer(toFix);
		String toReturn = ""
		while(st.hasMoreTokens())
		{
			String a = st.nextToken();
			if(a.equals(a.toUpperCase()) && !a.contains(".")){
				if(dontUppercaseFirstLetter.contains(a))
				{
					toReturn += a.toLowerCase();
				}
				else
				{
					toReturn += new String(a.charAt(0)) + a.substring(1).toLowerCase();
				}
			}else{
				toReturn += a
			}
			toReturn += " ";
		}
		
		return toReturn.trim()
	}
	
	def verwerkActies()
	{
		def toReturn = []
		
		def i = 0
		while(params.get("rekening"+i))
		{
			def rekening = params.get("rekening"+i)
			def bedragString = params.get("bedrag"+i)
			def klantNaam = params.get("klant"+i)
			
			if(klantNaam)
			{
				Klant klant = Klant.findByNaam(klantNaam);
				if(!klant)
				{
					throw new RuntimeException("Klant met naam "+ klantNaam +" niet gevonden!")
				}
				
				NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
				Number number = format.parse(bedragString);
				double bedrag = number.doubleValue(); 
				
				Betaling b = new Betaling(bedrag: bedrag, klant: klant);
				if (!b.save()) {
					b.errors.each {
						println it
					}
				}
				
				toReturn.add(b)
				
				// Remember the link between the accountnumber and the customer for the next time
				KlantRekeningnummer kr = KlantRekeningnummer.findByRekening(rekening)
				if(!kr)
				{
					kr = new KlantRekeningnummer(klant: klant, rekening: rekening as String);
					if (!kr.save()) {
						kr.errors.each {
							println it
						}
					}
				}
			}
			else
			{
				// Nothing entered. Delete any previous connections from this account number with a customer
				KlantRekeningnummer kr = KlantRekeningnummer.findByRekening(rekening)
				if(kr)
				{
					kr.delete()
				}
			}
			i++;
		}
		
		// Store datum + number of this datum
		String eersteDatum = params.eersteDatum
		Instelling eersteDatumInstelling = Instelling.findOrCreateWhere(naam : INITIAL_DATE)
		eersteDatumInstelling.setWaarde(eersteDatum)
		eersteDatumInstelling.save()
		
		String hoeveelheidEersteDatum = params.hoeveelheidEersteDatum
		Instelling hoeveelheidEersteDatumInstelling = Instelling.findOrCreateWhere(naam : AMOUNT_FIRST_DATE)
		hoeveelheidEersteDatumInstelling.setWaarde(hoeveelheidEersteDatum)
		hoeveelheidEersteDatumInstelling.save()
		
		return [betalingen: toReturn]
	}
}
