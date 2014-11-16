<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="beheer">
	<title>Bijboekingen verwerkt</title>
</head>
<body>
	<div style="margin-bottom:5pt;">
		Verwerkte transacties:
	</div>
	<table>
	<g:each in="${betalingen}" var="betaling">
		<tr>
			<td><g:link controller="klant" action="show" id="${betaling.klant.id}">${betaling.klant.naam}</g:link></td>
			<td>&euro;<g:formatNumber number="${betaling.bedrag}" minFractionDigits="2" /></td>
		</tr>
	</g:each>
	</table>
	<div style="margin-top:5pt;">
		<g:link controller="mail" action="maillijst">Door naar de maillijst</g:link><br/>
		<g:link controller="beheer">Terug naar het beheer</g:link>
	</div>
</body>
</html>