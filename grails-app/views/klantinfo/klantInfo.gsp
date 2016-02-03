<!DOCTYPE html>
<html>
<head>
	<title>${id != "null"? "Klant informatie" : "Bestellingen en betalingen van alle klanten"}</title>
	
	<meta name="format-detection" content="telephone=no" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
	<meta name="HandheldFriendly" content="true" />
	<meta name="apple-mobile-web-app-capable" content="yes" />
	
	<link rel="icon" href="${resource(dir: 'images', file: 'favicon.png')}" type="image/png">
	<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
	<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
	
	<style type="text/css">
		body
		{
			background-color: black;
		}
	</style>
	<link type="text/css" href="${resource(dir: 'css', file: 'klantinfo.css')}" rel="stylesheet"/>
	
	<g:javascript library="jquery" plugin="jquery"/>
	
	<script type="text/javascript">
		var klantId = ${id};
		$(document).ready(function() {
			loadData();
		});
	</script>
</head>

<body>
<div id="pageContainer">
</div>
<script type="text/javascript">
	var baseUrl = "${createLink(uri: '/')}";
</script>
<script type="text/javascript" src="${resource(dir: 'js', file: 'klantinfo.js')}"></script>
</body>
</html>