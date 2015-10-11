
<%@ page import="stampos.Product" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-product" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(controller: 'beheer')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
				<g:if test="${session.loggedIn}">
					<li style="float:right;"><g:link controller="authentication" action="logout" params="[nameOfController: controllerName]"><g:message code="authentication.logout" /></g:link></li>
				</g:if>
			</ul>
		</div>
		<div id="list-product" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="naam" title="${message(code: 'product.naam.label', default: 'Naam')}" />
					
						<g:sortableColumn property="colliGrootte" title="${message(code: 'product.colliGrootte.label', default: 'Colli Grootte')}" />
					
						<g:sortableColumn property="sortering" title="${message(code: 'product.sortering.label', default: 'Sortering')}" />
					
						<g:sortableColumn property="zichtbaar" title="${message(code: 'product.zichtbaar.label', default: 'Zichtbaar')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${productInstanceList}" status="i" var="productInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${productInstance.id}">${fieldValue(bean: productInstance, field: "naam")}</g:link></td>
					
						<td>${fieldValue(bean: productInstance, field: "colliGrootte")}</td>
					
						<td>${fieldValue(bean: productInstance, field: "sortering")}</td>
					
						<td><g:formatBoolean boolean="${productInstance.zichtbaar}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${productInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
