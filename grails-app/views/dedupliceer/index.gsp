<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="beheer">
	<title>Dedupliceer</title>
</head>
<body>
<g:each in="${bestellingenSets}" var="bestellingenSet">
	<h3>
	${bestellingenSet[0].klant.naam}: 
	<g:each in="${bestellingenSet[0].bestelRegels}" var="bestelRegel" status="status">
		${bestelRegel}<g:if test="${status != bestellingenSet[0].bestelRegels.size() - 1}">, </g:if>
	</g:each>
	</h3>
	<g:each in="${bestellingenSet}" var="bestelling">
		<g:link controller="bestelling" action="show" id="${bestelling.id}">${bestelling}</g:link><br/>
	</g:each>
	<br/>
</g:each>
</body>
</html>