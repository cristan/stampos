<h2>${it.beginDate} - ${it.endDate}</h2>
      		
<table>
	<tr>
		<th class="productColumn">Product</th>
		<th class="otherColumn">Ingekocht</th>
		<th class="otherColumn">Besteld</th>
		<th class="otherColumn">Rendement</th>
	</tr>
      		
	<g:each in="${it.rendementLijst}" var="r">
		<tr>
			<td>${r.product.naam}</td>
			<td>${r.aantalIngekocht}</td>
			<td>${r.aantalBesteld}</td>
			<td>${r.percentage}</td>
		</tr>
	</g:each>
			
	<tr class="totaal">
		<td class="productColumn">Totaal</td>
		<td class="otherColumn">${it.totaalIngekocht}</td>
		<td class="otherColumn">${it.totaalBesteld}</td>        		
		<td class="otherColumn">${it.totalPercentage}</td>
	</tr>
</table>