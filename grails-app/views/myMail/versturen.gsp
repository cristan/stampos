<html>
<head>
	<meta name="layout" content="beheer">
	<title><g:message code="sending.mails.automatically" /></title>
	<style type="text/css">
		.warnings
		{
			background: none repeat scroll 0 0 #FFF9F3;
		    border: 1px solid #FFD4AA;
		    box-shadow: 0 0 0.25em #FFC388;
		    color: #CC6600;
		    
		    
		    font-size: 0.8em;
		    line-height: 2;
		    margin: 1em 2em;
		    padding: 0.25em;
		}
		
	</style>
</head>
<body>
<g:if test="${grailsApplication.config.grails.mail.disabled}">
<div class="warnings">
In deze omgeving worden e-mails niet daadwerkelijk gestuurd.
</div>
</g:if>
<g:else>
<g:if test="${grailsApplication.config.grails.mail.overrideAddress}">
	<div class="warnings">
	In deze omgeving worden alle e-mails verstuurd naar ${grailsApplication.config.grails.mail.overrideAddress}
	</div>
</g:if>
</g:else>
 


<g:form>
<g:actionSubmit value="Verstuur berichten" action="doVerstuur"/>
<g:if test="${automailEnabled}">
	<br/>
	Elke zondag om 12.00 uur e-mails verzenden staat <strong>aan</strong>
	<g:actionSubmit value="Zet uit" action="disableAutomail"/>
</g:if>
<g:else>
	<br/>
	Elke zondag om 12.00 uur e-mails verzenden staat <strong>uit</strong>
	<g:actionSubmit value="Zet aan" action="enableAutomail"/>
</g:else>

</g:form>
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
