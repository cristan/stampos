<!DOCTYPE html>
<html>
<head>
<title>StamPOS <g:meta name="app.version"/></title>
<!-- This meta tag also implies that a browserconfig.xml exists (which it does) -->
<meta name="application-name" content="StamPOS"/>
<link rel="icon" href="${resource(dir: 'images', file: 'favicon.png')}" type="image/png">
<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
<link type="text/css" href="${resource(dir: 'css', file: 'loading.css')}" rel="stylesheet"/>
<link type="text/css" href="${resource(dir: 'css', file: 'style.css')}" rel="stylesheet"/>
<link type="text/css" href="${resource(dir: 'css', file: 'home.css')}" rel="stylesheet"/>
<g:if test="${grails.util.Environment.current.name != 'test'}">
	<link type="text/css" href="${resource(dir: 'css', file: 'extra_space_to_the_right.css')}" media="(min-width: 1920px)" rel="stylesheet" />
</g:if>
<g:javascript library="jquery" plugin="jquery"/>
<asset:javascript src="spring-websocket" />

<script type="text/javascript" src="${resource(dir: 'js', file: 'javascript.js')}"></script>
<script>
$(document).ready(function () {
	startStamPOS(${allowedToOrder});
});
</script>

<script type="text/javascript">
    $(function() { 
        var socket = new SockJS("${createLink(uri: '/stomp')}");
        var client = Stomp.over(socket);

        client.connect({}, function() {
            client.subscribe("/topic/user", function(message) {
                customerUpdated(message.body);
            });
        });
    });
</script>

</head>

<body>
<div id="loading">
	<div id="squaresWaveG">
		<div id="squaresWaveG_1" class="squaresWaveG">
		</div>
		<div id="squaresWaveG_2" class="squaresWaveG">
		</div>
		<div id="squaresWaveG_3" class="squaresWaveG">
		</div>
		<div id="squaresWaveG_4" class="squaresWaveG">
		</div>
		<div id="squaresWaveG_5" class="squaresWaveG">
		</div>
		<div id="squaresWaveG_6" class="squaresWaveG">
		</div>
		<div id="squaresWaveG_7" class="squaresWaveG">
		</div>
		<div id="squaresWaveG_8" class="squaresWaveG">
		</div>
	</div>
</div>

<div id="userButtonsArea">
	
</div>
<div id="noUsersNotice">
	Geen klanten gevonden. Maak <g:link controller="klant">klanten</g:link> aan in <g:link controller="beheer">het beheer</g:link>.
</div>
<div id="right">
	<div id="productButtonsArea"></div>
	<div id="noProductsNotice">Geen producten met prijs gevonden. Maak <g:link controller="product">producten</g:link> aan en koppel hier <g:link controller="productPrijs">productPrijzen</g:link> aan.</div>
	<div id="order">
	</div>
</div>
<div id="orderArea" class="orderArea">
	<span id="userName"></span><span id="creditInfo" style="display:none">: &euro;<span id="credit"></span> tegoed</span>
	<span id="userBlockedNotice">
		<img alt="Lock" src="${resource(dir: 'images', file: 'stock_lock_36.png')}"> geblokkeerd
	</span>
</div>
<div id="orderActionsArea">
	<div id="orderActions" style="width:200%;">
		<div id="clearOrderDiv">
			<button id="clearOrderSmall" class="clearOrder">Verwijder bestelling</button>
		</div>
		<div style="width:25%;padding-left:5pt;float:left;">
			<button id="confirmOrder">Bevestig bestelling</button>
		</div>
	</div>
</div>
<div class="orderActionsCover"></div>
<r:layoutResources/>
</body>
</html>