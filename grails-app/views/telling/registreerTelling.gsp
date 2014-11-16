<html>
    <head>
        <meta http-equiv="Content-type" content="text/html; charset=utf-8">
        <meta name="layout" content="beheer">
        <title>Registreer telling</title>
        <style type="text/css">
		table{
			width: auto;
		}
	</style>
    </head>
    <body>
        <g:form name="myForm" action="verwerkTelling">
	        <h1>Registreer telling</h1>
	        <table>
		        <g:each in="${producten}" var="product">
		            <tr>
		            	<td>${product.naam}</td>
		            	<td>
		            		<g:field type="number" name="aantalKeerProduct${product.id}"/>
		            	</td>
		            </tr>
		     	</g:each>
		     	<tr>
		     		<td></td>
		     		<td style="text-align:right">
		     			<g:submitButton name="submit" value="Verwerk" event=""/>
		     		</td>
		     	</tr>
	     	</table>
     	</g:form>
    </body>
</html>