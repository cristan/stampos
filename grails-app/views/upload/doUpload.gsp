<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="beheer">
	<title>Bijboekingen</title>
	<g:javascript library="jquery" plugin="jquery"/>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'jquery-ui-1.10.3.custom.min.js')}"></script>
	<link href="${resource(dir: 'css/ui-lightness', file: 'jquery-ui-1.10.3.custom.css')}" rel="stylesheet">
	
	<style type="text/css">
		body{
			font-family: Sans-Serif;
		}
		
		.transactie{
			margin-bottom:10pt;
		}
		
		.datum{
			line-height: 12pt;
			font-size:10pt;
			float:left;
			margin-right: 5pt;
		}
		
		.naamOmschrijving{
			line-height: 12pt;
			font-size: 12pt;
			float:left;
		}
		
		.mededelingen{
			clear:both;
			font-size: 12pt;
		}
	</style>
	
	<script type="text/javascript">
	$(function() {
		var availableTags = ${raw(klantnamen)};
		$( ".autocomplete" ).autocomplete({
			source: availableTags
		});
	});
	</script>
</head>
<body>
	<!-- TODO: use HTML5 list http://www.wufoo.com/html5/attributes/05-list.html -->
	<g:form name="myForm" action="verwerkActies">
		<g:hiddenField name="eersteDatum" value="${eersteDatum}"/>
		<g:hiddenField name="hoeveelheidEersteDatum" value="${hoeveelheidEersteDatum}"/>
		
		<g:if test="${newTransactions}">
			<g:each in="${newTransactions}" var="transactie" status="i">
				<div class="transactie">
					<div class="datum">${transactie.datum}</div>
					<div class="naamOmschrijving">${transactie.naamOmschrijving}</div>
					<div class="mededelingen">
						${transactie.mededelingen}
					</div>
					<div class="bedrag">
						&euro;${transactie.bedrag} =><g:hiddenField name="rekening${i}" value="${transactie.tegenrekening}"/><g:hiddenField name="bedrag${i}" value="${transactie.bedrag}" /> <g:textField name="klant${i}" value="${transactie.gevondenKlantNaam}" class="autocomplete" />
					</div>
				</div>
			</g:each>
			
			<g:submitButton name="mySubmit" value="Verwerk"/>
		</g:if>
		<g:if test="${!newTransactions}">
			Geen nieuwe transacties gevonden
		</g:if>
		
		<g:if test="${oldTransactions}">
			<h1>Oude transacties</h1>
			<g:each in="${oldTransactions}" var="transactie" status="i">
				<div class="transactie">
					<div class="datum">${transactie.datum}</div>
					<div class="naamOmschrijving">${transactie.naamOmschrijving}</div>
					<div class="mededelingen">
						${transactie.mededelingen}
					</div>
					<div class="bedrag">
						 &euro;${transactie.bedrag}
					</div>
				</div>
			</g:each>
		</g:if>
	</g:form>
</body>
</html>