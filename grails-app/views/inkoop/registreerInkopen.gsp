<html>
    <head>
        <meta http-equiv="Content-type" content="text/html; charset=utf-8">
        <meta name="layout" content="beheer">
        <title>Registreer inkopen</title>
        <style type="text/css">
        	/* apply a natural box layout model to all elements */
			* { -moz-box-sizing: border-box; -webkit-box-sizing: border-box; box-sizing: border-box; }
        
        	input[type='text'],input[type='number']{
        		width: 100%;
        	}
        	
        </style>
        <g:javascript library="jquery" plugin="jquery"/>
        <script type="text/javascript">
			<g:applyCodec encodeAs="none">
				var alleProducten = ${alleProductenAsJson};
			</g:applyCodec>

    		$(document).ready(function() {
				for(var i = 0; i < alleProducten.length; i++)
				{
					var product = alleProducten[i];
					$("#aantalKeerColli"+product.id).on('change', {product: product}, valueChanged);
					$("#aantalKeerColli"+product.id).on('keyup', {product: product}, valueChanged);
					$("#colliGrootte"+product.id).on('change', {product: product}, valueChanged);
					$("#colliGrootte"+product.id).on('keyup', {product: product}, valueChanged);
				}
	       	});

	       	function valueChanged(event)
	       	{
	       		var id = event.data.product.id;
				var aantal = $("#aantalKeerColli"+id).val() * $("#colliGrootte"+id).val();
				if(isNaN(aantal))
				{
					$("#aantal"+id).val("");
				}
				else
				{
					$("#aantal"+id).val(aantal);
				}
		    }
        </script>
    </head>
    <body>
        <g:form name="myForm" action="verwerkInkopen">
	        <h1>Registreer inkopen</h1>
	        <table>
	        
	        <g:each in="${producten}" var="product">
	            <tr>
	            	<td>${product.naam}</td>
	            	<td>
	            		<g:field type="number" name="aantalKeerColli${product.id}"/>
	            	</td>
	            	<td>x</td>
	            	<td>
	            		<g:field type="number" name="colliGrootte${product.id}" value="${product.colliGrootte}"/>
	            	</td>
	            	<td>=</td>
	            	<td>
	            		<g:field type="number" name="aantal${product.id}"/>
	            	</td>
	            </tr>
	     	</g:each>
	     	</table>
	     	<div style="text-align:right">
	     		<g:submitButton name="submit" value="Verwerk" event=""/>
	     	</div>
     	</g:form>
    </body>
</html>