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
	
	<g:render template="/templates/klantLijst" />
	
</body>
</html>