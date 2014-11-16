package stampos

import java.text.DateFormat;
import java.text.SimpleDateFormat

class UitstelController {

	def klantService
	String totString
	
	static DateFormat format = new SimpleDateFormat("dd MMMMMMMMMMMMMMMMMMM yyyy kk:mm")
	
    def uitstel()
	{ 
		totString = format.format(klantService.komendeMaandag())
		return [datum: totString]
	}
	
	def geefUitstel()
	{
		Date tot = klantService.komendeMaandag()
		
		for(Klant klant : Klant.all)
		{
			klant.uitstelTot = tot
		}
		return [datum: totString]
	}
}
