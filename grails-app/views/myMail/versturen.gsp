<html>
<head>
	<meta name="layout" content="beheer">
	<title><g:message code="sending.mails.automatically" /></title>
	<link rel='stylesheet' type="text/css" href="${resource(dir: 'css', file: 'warnings.css')}" />
</head>
<body>
<g:render template="/templates/emailsettingsNotice" />

<g:if test="${emailSettingsSet}">
	<g:form>
		<g:actionSubmit value="Verstuur berichten" action="doVerstuur"/>
		<br/>
		<g:if test="${automailEnabled}">
			Elke zondag om 12.00 uur e-mails verzenden staat <strong>aan</strong>
			<g:actionSubmit value="Zet uit" action="disableAutomail"/>
			<br/>		
			<g:if test="${automailWhenFinancesNotUpdated}">
				Deze mails worden <strong>ook</strong> verstuurd wanneer de financien niet geupdatet zijn. <g:actionSubmit value="Mail dan niet" action="disableAutomailWhenFinancesNotUpdated"/>
			</g:if>
			<g:else>
				Deze mails worden <strong>niet</strong> verstuurd wanneer de financien niet geupdatet zijn. <g:actionSubmit value="Mail dan wel" action="enableAutomailWhenFinancesNotUpdated"/>
			</g:else>
		</g:if>
		<g:else>
			Elke zondag om 12.00 uur e-mails verzenden staat <strong>uit</strong>
			<g:actionSubmit value="Zet aan" action="enableAutomail"/>
		</g:else>
	</g:form>
</g:if>
<br/>
<g:if test="${metMail.size==1}"><strong>Er is 1 klant met een e-mail adres</strong><br/></g:if>
<g:if test="${metMail.size!=1}"><strong>Er zijn ${metMail.size} klanten met een e-mail adres</strong><br/></g:if>
<g:each in="${metMail}" var="klant" status="status">
<g:link controller="klant" action="show" id="${klant.id}">${klant.naam}</g:link><g:if test="${status != metMail.size - 1}">,</g:if>
</g:each><br/>
<br/>
<g:if test="${zonderMail.size==1}"><strong>Er is 1 klant zonder een e-mail adres</strong><br/></g:if>
<g:if test="${zonderMail.size!=1}"><strong>Er zijn ${zonderMail.size} klanten zonder een e-mail adres</strong><br/></g:if>
<g:each in="${zonderMail}" var="klant" status="status">
<g:link controller="klant" action="show" id="${klant.id}">${klant.naam}</g:link><g:if test="${status != zonderMail.size - 1}">,</g:if>
</g:each><br/>
</body>
</html>
