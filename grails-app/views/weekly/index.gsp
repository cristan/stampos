<%@page import="stampos.NumberUtils"%>
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
			
			.mainColumn{
				width: 40%;
			}
			
			.otherColumn{
				width: 20%;
			}
        </style>
        <title>Besteld en betaald</title>
    </head>
    <body>
        <h1>Besteld en betaald</h1>
        
        <g:if test="${noData}">
        	<em>Er is niets besteld of betaald in deze periode</em>
        </g:if>
        <g:if test="${!noData}">
        	<table>
				<tr>
					<th class="mainColumn">Product</th>
					<th class="otherColumn">Besteld</th>
					<th class="otherColumn">Omzet</th>
				</tr>
			      		
				<g:each in="${besteld}" var="b">
					<tr>
						<td>${b[0]}</td>
						<td>${b[1]}</td>
						<td>${NumberUtils.formatMoney(b[2])}</td>
					</tr>
				</g:each>
				
				<tr class="totaal">
					<td class="mainColumn">Totaal</td>
					<td class="otherColumn">${totalAmount}</td>        		
					<td class="otherColumn">${NumberUtils.formatMoney(totalRevenue)}</td>
				</tr>
			</table>
        </g:if>
    </body>
</html>