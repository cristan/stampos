<%@page import="stampos.NumberUtils"%>
<g:if test="${klantLijst}">

	<!-- Please note that text colors etc have to be inline styles, or else the mail client won't pick it up -->
	<table>
	<g:each in="${klantLijst}" var="item">
            <tr>
            	<td>${item.naam}</td>
            	<g:if test="${item.tegoed < 0}">
            		<td style="color:#770000;">&euro;${NumberUtils.formatNumber(item.tegoed)}</td>
            	</g:if>
            	<g:if test="${item.tegoed >= 0}">
            		<td>&euro;${NumberUtils.formatNumber(item.tegoed)}</td>
            	</g:if>
            </tr>
     </g:each>
     </table>
 </g:if>
    
<g:if test="${!klantLijst}">(geen klanten gevonden)</g:if>