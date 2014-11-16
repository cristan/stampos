<html>
    <head>
        <meta http-equiv="Content-type" content="text/html; charset=utf-8">
        <meta name="layout" content="beheer">
        <style type="text/css">
        	td{
				line-height: normal;
				padding: 0.2em 0.5em;
			}
			
			.totaal{
				font-weight: bold;
			}
			
			.productColumn{
				width: 40%;
			}
			
			.otherColumn{
				width: 20%;
			}
        </style>
        <title>Geschat rendement</title>
    </head>
    <body>
        <h1>Geschat rendement</h1>
        
        <g:if test="${noData}">
        	<em>No data found</em>
        </g:if>
        <g:if test="${!noData}">
        	<g:render template="rendement" collection="${periodes}" />
        	
        	<g:if test="${periodes.size() != 1}">
	        	<h1>Totaal:</h1>
	        	<g:render template="rendement" bean="${totaal}" />
        	</g:if>
        </g:if>
    </body>
</html>