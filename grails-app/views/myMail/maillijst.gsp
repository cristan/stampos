<html>
<head>
	<meta name="layout" content="beheer">
	<title><g:message code="maillijst" /></title>
	<style type="text/css">
		/* Undo grails table CSS */
		table{
			width: auto;
			margin: 0.5em 0;
			border:0;
		}
		
		td{
			line-height: normal;
			padding: 0 0.5em;
		}
		
		td:hover, tr:hover{
			background: none;
		}
	</style>
	
	<link rel='stylesheet' type="text/css" href="${resource(dir: 'css', file: 'warnings.css')}" />
	<g:javascript library="jquery" plugin="jquery"/>
	<script type="text/javascript">
	function selectElementText(el, win) {
	    win = win || window;
	    var doc = win.document, sel, range;
	    if (win.getSelection && doc.createRange) {
	        sel = win.getSelection();
	        range = doc.createRange();
	        range.selectNodeContents(el);
	        sel.removeAllRanges();
	        sel.addRange(range);
	    } else if (doc.body.createTextRange) {
	        range = doc.body.createTextRange();
	        range.moveToElementText(el);
	        range.select();
	    }
	}

	</script>
	
	<script>
		function automailChanged(checkbox)
		{
			alert(checkbox.is(':checked'))
		}

		$(document).ready(function () {
			$('#automail').on("change", function(){
				$("#recipient").prop('disabled', !this.checked);
			});
		});
	</script>
</head>
<body>
	<g:render template="/templates/emailsettingsNotice" />
	
	<g:form action="submitSettings">
		<g:checkBox name="automail" checked="${automailListEnabled}" />
		<label for="automail"><g:if test="${klantLijst}">E-mail dit wekelijks naar</g:if><g:if test="${!klantLijst}">E-mail een lijst met klanten en hun rekeningen wekelijks naar</g:if></label> <g:field type="email" name="recipient" value="${recipient}" placeholder="ontvanger" required="true" disabled="${!automailListEnabled}"/>
		<g:submitButton name="submit" value="Verstuur"/>
	</g:form>
	<br/>
	
	<g:if test="${klantLijst}">
		<br/>
		
		<button onclick="selectElementText(document.getElementById('theTable'));">Selecteer de maillijst</button>
		<!-- TODO: sloop deze shit er uit -->
		<a href="mailto:${to}?subject=${subject}" target="_blank">Stuur handmatig een e-mail</a><br/>
	
		<!-- Please note that text colors etc have to be inline styles, or else the mail client won't pick it up -->
		<table id="theTable">
		<g:each in="${klantLijst}" var="item">
	            <tr>
	            	<td>${item.naam}</td>
	            	<g:if test="${item.tegoed < 0}">
	            		<td style="color:#770000;">&euro;${item.tegoed}</td>
	            	</g:if>
	            	<g:if test="${item.tegoed >= 0}">
	            		<td>&euro;${item.tegoed}</td>
	            	</g:if>
	            </tr>
	     </g:each>
	     </table>
	 </g:if>
     
     <g:if test="${!klantLijst}">(geen klanten gevonden)</g:if>
</body>
</html>