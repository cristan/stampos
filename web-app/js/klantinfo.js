$(document).ready(function() {
	loadData();

	if(!klantId)
	{
		var socket = new SockJS(baseUrl + 'stomp');
        var client = Stomp.over(socket);

        // TODO: also do this for when you _are_ viewing a specific user?
        client.connect({}, function() {
        	client.subscribe("/topic/order", function(message) {
        		$("#pageContainer").prepend(getOrderHtml(JSON.parse(message.body)));
        	});
        	client.subscribe("/topic/payment", function(message) {
        		$("#pageContainer").prepend(getPaymentHtml(JSON.parse(message.body)));
        	});
        });
	}
});

var eindeDatum;

var win = $(window),doc = $(document);
var loading = false;
var shouldLoad = false;
var endOfTheLine = false;
var firstTimeLoaded = false;

function loadData()
{
	loading = true;
	var url = baseUrl + 'klantmutaties';
	if(eindeDatum)
	{
		url += '?beginDatum='+eindeDatum+'&klantId='+klantId+'&callback=?';
	}
	else
	{
		url += '?klantId='+klantId+'&callback=?';
	}
	
	$.getJSON(url, function(data) {
		for(var i = 0; i < data.items.length; i++)
		{
			var item = data.items[i];
			if(item.bestelling)
			{
				$("#pageContainer").append(getOrderHtml(item));
			}
			if(item.betaling)
			{	
				$("#pageContainer").append(getPaymentHtml(item));
			}
		}

		eindeDatum = data.eindeDatum;
		
		if(eindeDatum == null)
		{
			endOfTheLine = true;
			if(!firstTimeLoaded && data.items.length == 0)
			{
				$("#pageContainer").append("<div id='noDataNotice'>Geen betalingen of bestellingen gevonden</div>");
			}
			else
			{
				$("#pageContainer").append("<div id='end'>Geen verdere betalingen of bestellingen gevonden</div>");
			}
		}
		
		loading = false;
		firstTimeLoaded = true;
		loadIfNeeded();
	});
}

$(document).ready(function() {
	loadData();

	if(klantId)
	{
		var socket = new SockJS("${createLink(uri: '/stomp')}");
        var client = Stomp.over(socket);

        client.connect({}, function() {
            client.subscribe("/topic/order", function(message) {
            	$("#pageContainer").append(getOrderHtml(message));
            });
        });
	}
});

function getOrderHtml(item)
{
	var klantnaam = item.klantnaam;
	var bestelling = item.bestelling;
	var headerContents;
	if(klantnaam)
	{
		headerContents = "Bestelling "+ klantnaam
	}
	else
	{
		headerContents = "Besteld"
	}
	var content = 
		"<div class='bestelling'>"+
				"<div class='bestellingHeader'>"+
				"<div class='headerType'>"+headerContents+"</div>"+
				"<div class='headerDate'>"+bestelling.datumFormatted+"</div>"+
				"<div class='clear'/>"+
			"</div>"+
			"<div class='bestellingContent'>";

	for(var j = 0; j < bestelling.bestelregels.length; j++)
	{
		var bestelregel = bestelling.bestelregels[j];
		content += 
			"<div class='bedrag'>&euro;"+bestelregel.totaalPrijs+"</div>"+
			"<div class='product'>"+ bestelregel.product +"</div>"+ 
			"<div class='aantal'>"+bestelregel.aantal +"</div>"+
			"<div class='clear'/>";
	}

	if(bestelling.bestelregels.length > 1)
	{
		content += "<div class='totalLine'/><div class='clear'/>";
		content += "<div class='totalOrder'>&euro;"+ bestelling.totaalBestelling +"</div><div class='clear'/>";
	}

	content +=					
			"</div>"+
		"</div>"
	return content;
}

function getPaymentHtml(item)
{
	var headerContents;
	var klantnaam = item.klantnaam;
	var betaling = item.betaling;
	if(klantnaam)
	{
		headerContents = "Betaling "+ klantnaam;
	}
	else
	{
		headerContents = "Betaling verwerkt";
	}
	var content = 
		"<div class='betaling'>"+
				"<div class='betalingHeader'>"+
				"<div class='headerType'>"+headerContents+"</div>"+
				"<div class='headerDate'>"+betaling.datumFormatted+"</div>"+
				"<div class='clear'/>"+
			"</div>"+
			"<div class='betalingContent'>";

	content += 
		"<div class='bedrag'>&euro;"+betaling.bedrag+"</div>"+
		"<div class='clear'/>";

	content +=					
			"</div>"+
		"</div>"
			
	return content;
}


function loadIfNeeded()
{
	//console.log(firstTimeLoaded +", "+ (win.scrollTop() > doc.height() - (win.height() * 2)) +", "+ !endOfTheLine)
	if(firstTimeLoaded && (win.scrollTop() > doc.height() - (win.height() * 2)) && !endOfTheLine)
	{
		if(!loading)
		{
			loading = true;
			loadData();
		}
	}
}

win.scroll(function()
{
	loadIfNeeded()
});