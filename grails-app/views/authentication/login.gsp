<!doctype html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Log in</title>
	</head>
	<body>
		<div style="margin: 0 1.5em">
			<g:if test="${error}">
				<div class="errors">
					${error}
				</div>
			</g:if>
			<g:if test="${flash.message}">
				<div class="message">${flash.message}</div>
			</g:if>
			
			<h1>Log in</h1>
			
			<g:form action="login">
				<label for="pass">Password:</label><input type="password" id="pass" name="password" placeholder="Enter password here">
				
				<g:if test="${nameOfController}">
					<g:hiddenField name="nameOfController" value="${nameOfController}" />
				</g:if>
				<g:if test="${nameOfAction}">
					<g:hiddenField name="nameOfAction" value="${nameOfAction}" />
				</g:if>
				<g:if test="${parameters}">
					<g:hiddenField name="parameters" value="${parameters}" />
				</g:if>
				<input type="submit" value="Log in">
			</g:form>
		</div>
	</body>
</html>