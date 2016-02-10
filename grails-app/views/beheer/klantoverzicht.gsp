<html>
    <head>
        <meta http-equiv="Content-type" content="text/html; charset=utf-8">
        <meta name="layout" content="beheer">
        <title>Bestellingen en betalingen van klanten</title>
    </head>
    <body>
        <g:link controller="klantinfo" action="klantInfo" id="all">Bestellingen en betalingen van alle klanten</g:link><br/>
        <br/>
        <h1>Bestellingen en betalingen van klanten</h1>
        
        <g:each in="${klanten}" var="klant">
        	<g:link controller="klantinfo" action="klantInfo" id="${klant.id}">
            	${klant.naam}
            </g:link>
            <br/>
            
        </g:each>
        <br/>
        <g:link controller="beheer">Terug naar het beheer</g:link>
    </body>
</html>