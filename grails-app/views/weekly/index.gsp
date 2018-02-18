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
        <div>
	        <g:link params="[beginDate: previousBeginDate, endDate: previousEndDate]">Vorige week</g:link>
	        
	        <span style="position:absolute;left: 50%;transform: translateX(-50%)">${beginDateFormatted} - ${endDateFormatted}</span>
	        <g:if test="${shouldShowLinkToNextWeek}">
	        	<g:link params="[beginDate: nextBeginDate, endDate: nextEndDate]" style="float:right">Volgende week</g:link>
	        </g:if>
        </div>
        <h1>Besteld</h1>
        
        <g:if test="${nothingOrdered}">
        	<em>Er is niets besteld in deze periode</em>
        </g:if>
        <g:if test="${!nothingOrdered}">
        	<table>
				<tr>
					<th class="mainColumn">Product</th>
					<th class="otherColumn">Besteld</th>
					<th class="otherColumn">Omzet</th>
				</tr>
			      		
				<g:each in="${ordered}" var="o">
					<tr>
						<td>${o[0]}</td>
						<td>${o[1]}</td>
						<td>${NumberUtils.formatMoney(o[2])}</td>
					</tr>
				</g:each>
				
				<tr class="totaal">
					<td class="mainColumn">Totaal</td>
					<td class="otherColumn">${totalAmount}</td>        		
					<td class="otherColumn">${NumberUtils.formatMoney(totalRevenue)}</td>
				</tr>
			</table>
        </g:if>
        
        <h1>Betaald</h1>
        
        <g:if test="${nothingPaid}">
        	<em>Er zijn geen betalingen in deze periode</em>
        </g:if>
        <g:if test="${!nothingPaid}">
        	<table>
				<tr>
					<th class="mainColumn">Naam</th>
					<th class="otherColumn">Betaald</th>
				</tr>
			      		
				<g:each in="${paid}" var="p">
					<tr>
						<td>${p.klant.naam}</td>
						<td>${NumberUtils.formatMoney(p.bedrag)}</td>
					</tr>
				</g:each>
				
				<tr class="totaal">
					<td class="mainColumn">Totaal</td>        		
					<td class="otherColumn">${NumberUtils.formatMoney(totalPaid)}</td>
				</tr>
			</table>
        </g:if>
    </body>
</html>