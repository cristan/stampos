<html>
<head>
	<meta name="layout" content="main">
	<title>Verwijder alle bestellingen</title>
</head>
<body>
	<g:if test="${flash.message}">
		<div class="errors">
			${flash.message}
		</div>
	</g:if>
	Er zijn bijna geen redenen om alle bestellingen te verwijderen. Vul hieronder ter controle nogmaals het wachtwoord van het beheer is als je hier alsnog verder mee wilt gaan.
	<br/>
	<br/>
	<g:form controller="verwijderAlleBestellingen">
	<label for="password">Wachtwoord beheer</label><g:passwordField name="password"/>
	<g:actionSubmit value="Verwijder alle bestellingen" action="verwijderd"/>
	</g:form>
</body>
</html>