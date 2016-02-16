package stampos

import java.text.DecimalFormat

class NumberUtils {
	static DecimalFormat numberFormat
	
	public static String formatNumber(def number)
	{
		if(number == 0)
		{
			return "0"
		}
		else
		{			
			if(numberFormat == null)
			{
				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(2);
				df.setMinimumFractionDigits(2);
				numberFormat = df;
			}
			return numberFormat.format(number)
		}
	}
	
	public static String formatMoney(def value)
	{
		return "\u20AC" + formatNumber(value)
	}
}
