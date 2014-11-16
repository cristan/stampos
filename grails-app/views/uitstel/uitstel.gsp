<html>
<head>
	<meta name="layout" content="beheer">
	<title>Geef iedereen uitstel</title>
	</script>
</head>
<body>
	Hiermee geef je iedereen uitstel tot ${datum}. Tot die tijd wordt niemand geblokkeerd.<br/>
	<br/>
	<g:form controller="uitstel">
	<g:actionSubmit value="Geef iedereen uitstel" action="geefUitstel"/>
	</g:form>
</body>
</html>