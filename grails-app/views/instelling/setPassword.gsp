<html>
<head>
	<meta name="layout" content="main"/>
	<title><g:message code="settings.set_password" /></title>
</head>
<body>
	<!-- Navigation bar to home and a log out button -->
	<div class="nav" role="navigation">
		<ul>
			<li><a class="home" href="${createLink(controller: 'beheer')}"><g:message code="default.home.label"/></a></li>
			<g:if test="${session.loggedIn}">
				<li style="float:right;"><g:link controller="authentication" action="logout" params="[nameOfController: controllerName]"><g:message code="authentication.logout" args="[nameOfController: controllerName]" /></g:link></li>
			</g:if>
		</ul>
	</div>
	<div class="content">
		<h1><g:message code="settings.set_password"/></h1>
		<g:if test="${flash.message}">
			<div class="message">${flash.message}</div>
		</g:if>
		<g:form action="doSetPassword">
			<fieldset class="form">
				<div class="fieldcontain required">
				<label for="password1"><g:message code="settings.set_password.password"/><span class="required-indicator">*</span></label>
				<g:passwordField name="password1" required="true"/>
				</div>
				<div class="fieldcontain required">
				<label for="password2"><g:message code="settings.set_password.password_2"/><span class="required-indicator">*</span></label>
				<g:passwordField name="password2" required="true"/><br/>
				</div>
			</fieldset>
			<fieldset class="buttons">
				<g:submitButton name="submitButton" class="save" value="${g.message(code:'save')}"/>
			</fieldset>
		</g:form>
	</div>
</body>
</html>