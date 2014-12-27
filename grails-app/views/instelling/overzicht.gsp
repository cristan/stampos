<html>
<head>
	<meta name="layout" content="beheer">
	<title><g:message code="settings" /></title>
</head>
<body>
	<g:if test="${flash.message}">
		<div class="message">${flash.message}</div>
	</g:if>
	<g:form action="submit">
		<label for="allowRequestsFrom"><g:message code="settings.allow_requests_from"/></label>
		<g:select name="allowRequestsFrom" from="${['localhost', 'local_network', 'everywhere']}"
		          valueMessagePrefix="settings.allow_requests_from" value="${allowRequests}"/><br/>
		<g:submitButton name="submitButton" value="${g.message(code:'submit')}"/>
	</g:form>
</body>
</html>