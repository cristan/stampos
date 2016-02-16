<html>
<head>
	<meta name="layout" content="beheer">
	<title><g:message code="settings" /></title>
	<style>
		#emailSettings{
			display:inline-block;
		}
		
		#emailSettings input{
			float:right;
			margin-bottom: 3pt;
		}
		
		#emailSettings h2{
			clear:both;
		}
		
		#emailSettings label{
			float:left;
			clear:both;
			line-height: 16px;
		}
		
		#emailSettings span
		{
			float:left;
			font-size: 12px;
			line-height: 16px;
		}
		
	</style>
</head>
<body>
	<g:if test="${flash.message}">
		<div class="message">${flash.message}</div>
	</g:if>
	<g:form action="submit">
		<h1><g:message code="settings.security"/></h1>
		<g:link action="changePassword"><g:message code="settings.change_admin_password"/></g:link><br/>
		<br/>
		<label for="allowRequestsFrom"><g:message code="settings.allow_requests_from"/></label>
		<g:select name="allowRequestsFrom" from="${['localhost', 'local_network', 'everywhere']}"
		          valueMessagePrefix="settings.allow_requests_from" value="${allowRequests}"/><br/>
		<g:submitButton name="submitButton" value="${g.message(code:'save')}"/>
	</g:form>
	<br/>
	<h1><g:message code="settings.email"/></h1>
	<g:form action="submitEmail">
		<h2><g:message code="settings.email.smtp"/></h2>
		<span style="font-size:10pt;">Note: schakel <a href="https://www.google.com/settings/security/lesssecureapps">toegang voor apps met lagere beveiliging</a> in als je van gmail gebruik wilt maken.<br/>
		<br/></span>
		<div id="emailSettings">
			<label for="smtphost"><g:message code="settings.email.smtp.host"/></label><span class="example">bijv. smtp.gmail.com</span><g:textField name="smtphost" value="${smtphost}"/><br/>
			<label for="smtpport"><g:message code="settings.email.smtp.port"/></label><g:field type="number" name="smtpport" value="${smtpport}"/><br/>
			<label for="smtp_require_ssl"><g:message code="settings.email.smtp.require_ssl"/></label><g:checkBox name="smtp_require_ssl" checked="${smtp_require_ssl}"/><br/>
			<label for="smtp_use_tls"><g:message code="settings.email.smtp.use_tls"/></label><g:checkBox name="smtp_use_tls" checked="${smtp_use_tls}"/><br/>
			<label for="smtpusername"><g:message code="settings.email.smtp.username"/></label><g:textField name="smtpusername" value="${smtpusername}"/><br/>
			<label for="smtppassword"><g:message code="settings.email.smtp.password"/></label><br/>
			<span style="font-size:10pt;float:left;margin-right: 2pt;clear:left;">(let op: het is hierna mogelijk voor iedereen met toegang tot dit beheer om je wachtwoord uit te lezen)</span><g:passwordField name="smtppassword" value="${smtppassword}"/><br/>
			
			<br/>
			<h2><g:message code="settings.email.sender"/></h2>
					
			<label for="sendername"><g:message code="settings.email.sendername"/></label><g:textField style="float:right" name="sendername" value="${sendername}"/><br/>
			<label for="senderemail"><g:message code="settings.email.senderemail"/></label><g:field type="email" name="senderemail" value="${senderemail}"/><br/>
			
			<br/>
			<h2><g:message code="settings.email.contents"/></h2>
			<label for="titleInCaseOfCredit"><g:message code="settings.email.title.credit"/></label><g:textField name="titleInCaseOfCredit" value="${titleInCaseOfCredit}"/><br/>
			<label for="titleInCaseOfDebit"><g:message code="settings.email.title.debit"/></label><g:textField name="titleInCaseOfDebit" value="${titleInCaseOfDebit}"/><br/>
			<label for="accountiban"><g:message code="settings.email.account.iban"/></label><g:textField name="accountiban" value="${accountiban}"/><br/>
			<label for="accountowner"><g:message code="settings.email.account.owner"/></label><g:textField name="accountowner" value="${accountowner}"/><br/>
			<label for="serverurl"><g:message code="settings.email.server.url"/></label><g:textField name="serverurl" value="${serverurl}"/><br/>
			
			<br/>
			<g:submitButton name="submitButton" value="${g.message(code:'save')}" style="clear:both;"/>
		</div>
	</g:form>
	<br/>
	Automatisch versturen persoonlijke e-mails: <g:link controller="myMail" action="versturen"><g:message code="sending.mails.automatically" /></g:link><br/>
	Automatisch versturen maillijst: <g:link controller="myMail" action="maillijst"><g:message code="maillijst" /></g:link>
</body>
</html>