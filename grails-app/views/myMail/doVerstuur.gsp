<html>
<head>
	<meta name="layout" content="beheer">
	<title>E-mails zijn verstuurd</title>
</head>
<body>

<g:if test="${klantenMetRekening.size == 1}">
	<g:message code="mail.mailed_to_customers_who_have_to_pay.1" /><br/>
</g:if>
<g:else>
	<g:message code="mail.mailed_to_customers_who_have_to_pay.n" args="${[klantenMetRekening.size]}" /><br/>
</g:else>

<g:if test="${klantenMetTegoedGemaild.size == 1}">
	<g:message code="mail.mailed_to_customers_who_dont_have_to_pay.1" /><br/>
</g:if>
<g:else>
	<g:message code="mail.mailed_to_customers_who_dont_have_to_pay.n" args="${[klantenMetTegoedGemaild.size]}"/><br/>
</g:else>

<g:if test="${klantenMetTegoedNietGemaild.size == 1}">
	<g:message code="mail.not_mailed.1" /><br/>
</g:if>
<g:else>
	<g:message code="mail.not_mailed.n" args="${[klantenMetTegoedNietGemaild.size]}"/><br/>
</g:else>

<br/>
<g:link controller="beheer">Terug naar het beheer</g:link>
</body>
</html>
