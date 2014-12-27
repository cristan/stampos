<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>StamPOS beheer</title>
		<style type="text/css" media="screen">
			#status {
				background-color: #eee;
				border: .2em solid #fff;
				margin: 2em 2em 1em;
				padding: 1em;
				width: 12em;
				float: left;
				-moz-box-shadow: 0px 0px 1.25em #ccc;
				-webkit-box-shadow: 0px 0px 1.25em #ccc;
				box-shadow: 0px 0px 1.25em #ccc;
				-moz-border-radius: 0.6em;
				-webkit-border-radius: 0.6em;
				border-radius: 0.6em;
			}

			.ie6 #status {
				display: inline; /* float double margin fix http://www.positioniseverything.net/explorer/doubled-margin.html */
			}

			#status ul {
				font-size: 0.9em;
				list-style-type: none;
				margin-bottom: 0.6em;
				padding: 0;
			}

			#status li {
				line-height: 1.3;
			}

			#status h1 {
				text-transform: uppercase;
				font-size: 1.1em;
				margin: 0 0 0.3em;
			}

			#page-body {
				margin: 2em 1em 1.25em 18em;
			}

			h2 {
				margin-top: 1em;
				margin-bottom: 0.3em;
				font-size: 1em;
			}

			p {
				line-height: 1.5;
				margin: 0.25em 0;
			}

			#controller-list ul {
				list-style-position: inside;
			}

			#controller-list li {
				line-height: 1.3;
				list-style-position: inside;
				margin: 0.25em 0;
			}

			@media screen and (max-width: 480px) {
				#status {
					display: none;
				}

				#page-body {
					margin: 0 1em 1em;
				}

				#page-body h1 {
					margin-top: 0;
				}
			}
		</style>
	</head>
	<body>
		<a href="#page-body" class="skip"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="status" role="complementary">
			<h1>Application Status</h1>
			<ul>
				<li>App version: <g:meta name="app.version"/></li>
				<li>Grails version: <g:meta name="app.grails.version"/></li>
				<li>Groovy version: ${GroovySystem.getVersion()}</li>
				<li>JVM version: ${System.getProperty('java.version')}</li>
				<li>Reloading active: ${grails.util.Environment.reloadingAgentEnabled}</li>
				<li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
				<li>Domains: ${grailsApplication.domainClasses.size()}</li>
				<li>Services: ${grailsApplication.serviceClasses.size()}</li>
				<li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
			</ul>
			<h1>Installed Plugins</h1>
			<ul>
				<g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
					<li>${plugin.name} - ${plugin.version}</li>
				</g:each>
			</ul>
		</div>
		<div id="page-body" role="main">
			<h1>StamPOS beheer</h1>
			<g:link controller="upload" action="upload">Upload dump internetbankieren</g:link><br/>
			<g:link controller="instelling" action="overzicht"><g:message code="settings" /></g:link><br/>
			<g:link controller="myMail" action="versturen">Verstuur e-mails</g:link><br/>
			<br/>
			<g:link controller="myMail" action="maillijst">Maillijst</g:link><br/>
			<g:link controller="inkoop" action="registreerInkopen">Registreer inkopen</g:link><br/>
			<g:link controller="telling" action="registreerTelling">Tel de inhoud van de bar</g:link><br/>
			<g:link url="${createLink(uri: '/deblokkeer')}">Deblokkeer klanten</g:link><br/>
			<g:link controller="uitstel" action="uitstel">Geef iedereen uitstel</g:link><br/>
			<g:link controller="verwijderAlleBestellingen">Verwijder alle bestellingen</g:link><br/>
			<br/>
			<g:link controller="rendement" action="schatting">Rendement van de bar (schatting nav inkopen)</g:link><br/>
			<g:link controller="rendement" action="geteld">Rendement van de bar (nav telling)</g:link><br/>
			<br/>
			<g:link controller="beheer" action="klantoverzicht">Bestellingen en betalingen van klanten</g:link><br/>
			<g:link controller="klant" params="[max: 100]" >Bekijk klanten</g:link><br/>
			<g:link controller="betaling">Bekijk betalingen</g:link><br/>
			<g:link controller="product">Bekijk producten</g:link><br/>
			<g:link controller="productPrijs">Bekijk productPrijzen</g:link><br/>
			<g:link controller="bestelling">Bekijk bestellingen</g:link><br/>
			<g:link controller="inkoop">Bekijk inkopen</g:link><br/>
			<g:link controller="telling">Bekijk tellingen</g:link><br/>

			<!-- 
			<div id="controller-list" role="navigation">
				<h2>Available Controllers:</h2>
				<ul>
					<g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
						<li class="controller"><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link></li>
					</g:each>
				</ul>
			</div>
			 -->
		</div>
	</body>
</html>
