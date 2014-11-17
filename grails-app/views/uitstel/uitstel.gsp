<html>
<head>
	<meta name="layout" content="beheer">
	<title>Geef iedereen uitstel</title>
</head>
<body>
	Hiermee geef je iedereen uitstel tot ${datum}. Tot die tijd mag iedereen bestellen ongeacht de hoogte van de rekening.<br/>
	<br/>
	<g:form controller="uitstel">
	<g:actionSubmit value="Geef iedereen uitstel" action="geefUitstel"/>
	</g:form>
</body>
</html>