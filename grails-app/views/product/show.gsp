
<%@ page import="stampos.Product" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-product" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(controller: 'beheer')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
				<g:if test="${session.loggedIn}">
					<li style="float:right;"><g:link controller="authentication" action="logout" params="[nameOfController: controllerName]"><g:message code="authentication.logout" /></g:link></li>
				</g:if>
			</ul>
		</div>
		<div id="show-product" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list product">
			
				<g:if test="${productInstance?.naam}">
				<li class="fieldcontain">
					<span id="naam-label" class="property-label"><g:message code="product.naam.label" default="Naam" /></span>
					
						<span class="property-value" aria-labelledby="naam-label"><g:fieldValue bean="${productInstance}" field="naam"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${productInstance?.colliGrootte}">
				<li class="fieldcontain">
					<span id="colliGrootte-label" class="property-label"><g:message code="product.colliGrootte.label" default="Colli Grootte" /></span>
					
						<span class="property-value" aria-labelledby="colliGrootte-label"><g:fieldValue bean="${productInstance}" field="colliGrootte"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${productInstance?.price}">
				<li class="fieldcontain">
					<span id="price-label" class="property-label"><g:message code="product.price.label" default="Prijs" /></span>
					
						<span class="property-value" aria-labelledby="price-label">&euro;<g:fieldValue bean="${productInstance}" field="price"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${productInstance?.sortering}">
				<li class="fieldcontain">
					<span id="sortering-label" class="property-label"><g:message code="product.sortering.label" default="Sortering" /></span>
					
						<span class="property-value" aria-labelledby="sortering-label"><g:fieldValue bean="${productInstance}" field="sortering"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${productInstance?.zichtbaar}">
				<li class="fieldcontain">
					<span id="zichtbaar-label" class="property-label"><g:message code="product.zichtbaar.label" default="Zichtbaar" /></span>
					
						<span class="property-value" aria-labelledby="zichtbaar-label"><g:formatBoolean boolean="${productInstance?.zichtbaar}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:productInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${productInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
