<html>
    <head>
        <meta http-equiv="Content-type" content="text/html; charset=utf-8">
        <meta name="layout" content="beheer">
        <title>Inkopen verwerkt</title>
    </head>
    <body>
        <h1>Inkopen verwerkt</h1>
        
        <g:each in="${inkoopRegels}" var="inkoopRegel">
        	<g:link controller="inkoopRegel" action="show" id="${inkoopRegel.id}">
            	${inkoopRegel.aantal} x ${inkoopRegel.product.naam}
            </g:link>
            <br/>
            
        </g:each>
        <br/>
        <g:link controller="beheer">Terug naar het beheer</g:link>
    </body>
</html>