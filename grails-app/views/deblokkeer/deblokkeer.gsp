<!DOCTYPE html>
<html>
<head>
	<title>StamPOS deblokkeer</title>
	
	<meta name="format-detection" content="telephone=no" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
	<meta name="HandheldFriendly" content="true" />
	<meta name="apple-mobile-web-app-capable" content="yes" />

	<link rel="shortcut icon" href="${resource(dir: 'images/faviconsbeheer', file: 'favicon.png')}" type="image/png">
	<link rel="apple-touch-icon" href="${resource(dir: 'images/faviconsbeheer', file: 'apple-touch-icon.png')}">
	<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images/faviconsbeheer', file: 'apple-touch-icon-retina.png')}">
	<meta name="msapplication-TileImage" content="${resource(dir: 'images/faviconsbeheer', file: 'favicon.png')}"/>
	
	<link type="text/css" href="${resource(dir: 'css', file: 'style.css')}" rel="stylesheet"/>
	<link type="text/css" href="${resource(dir: 'css', file: 'deblokkeer.css')}" rel="stylesheet"/>
	<g:javascript library="jquery" plugin="jquery"/>
	
	<script type="text/javascript" src="${resource(dir: 'js', file: 'jquery.timeago.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'jquery.timeago.nl.js')}"></script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'deblokkeer.js')}"></script>
</head>
<body>
<div style="padding:5pt 10pt 10pt;" id="pageContainer">
	<div id="login">
		<form onsubmit="logIn($('#passwordField').val());return false;">
			<input id="passwordField" type="password" placeholder="Wachtwoord">
			<button type="submit" value="Log in" style="float:right;padding:0 20pt;margin-top:10pt;">Log in</button>
		</form>
	</div>

	<div id="actions" style="display:none;">
		<div id="names">
		</div>
	</div>
</div>
</body>
</html>