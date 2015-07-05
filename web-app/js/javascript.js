var selectedKlant;
var selectedKlantGeblokkeerd = false;
var order = [];
var allowedToOrder;
var environmentName;

// Fancy ECMA-402 number formatting (which uses the default Locale because of the undefined).
// This is supported by Chrome 24, Fx 29 and IE11 (source: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Number/toLocaleString)
var formatter = new Intl.NumberFormat(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2});

function startStamPOS(allowedToOrder)
{
	var productenLoaded = false;
	var klantenLoaded = false;
	this.environmentName = environmentName;
	this.allowedToOrder = allowedToOrder;
	
	$.getJSON('product/all?callback=?', function(data) {
		$.each(data, function(i, product)
		{
			var test = $('<button/>',
		    {
		        text: product.naam,
		        click: function () { selectProduct($(this),product); }
		    });
		    test.addClass('productSelectButton');

			$("#productButtonsArea").append(test);
		});
		productenLoaded = true;

		if(klantenLoaded)
		{
			getThisShowOnTheRoad();
		}
	});
	
	$.getJSON('klant/all?callback=?', function(data) {
		$.each(data, function(i, klant)
		{
			var zeButton = $('<button/>',
		    {
		        text: klant.naam,
		        click: function () { selectKlant($(this),klant); }
		    });
			zeButton.addClass('userSelectButton');

			$("#userButtonsArea").append(zeButton);
		});
		klantenLoaded = true;

		if(productenLoaded)
		{
			getThisShowOnTheRoad();
		}
	});
	
	$("#clearOrderSmall").click(function() {
		clearOrder();
		clearSelectedKlantText();
	});
	
	$("#clearOrderBig").click(function() {
		clearOrder();
		clearSelectedKlantText();
	});
	
	$("#confirmOrder").click(function() {
		confirmOrder();
	});
}

var klantWeerTerugTimeout;

function getThisShowOnTheRoad()
{
	$("#loading").addClass("loadingDone");
	if(allowedToOrder)
	{
		$("#productButtonsArea").show();
		$("#productButtonsArea").addClass("productButtonsAreaShown");
	}
	$("#userButtonsArea").addClass("userButtonsAreaShown");
	setTimeout(function(){
		$("#loading").remove();// The opacity should already be 0 by now, but remove it just in case, maybe it'll help performance 
	}, 2000);
}



function selectKlant(clickedButton, klant)
{
	selectedKlantGeblokkeerd = false;
	$('.userSelectButton').removeClass('selected');
	
	clickedButton.addClass('selected');
	
	clearTimeout(naOrderKlantWegTimeout);
	if(selectedKlant == null)
	{
		$('#userBlockedNotice').hide();
		selectedKlant = klant;
		showInfoSelectedKlantPart1();
		showInfoSelectedKlantPart2(0);
		$("#orderArea").addClass("orderAreaVisible");
	}
	else
	{
		selectedKlant = klant;
		$("#orderArea").removeClass("orderAreaVisible");
		clearTimeout(klantWeerTerugTimeout);
		klantWeerTerugTimeout = setTimeout(function(){
			$('#userBlockedNotice').hide();
			showInfoSelectedKlantPart1();
			$("#orderArea").addClass("orderAreaVisible");
		}, 190);
		
		showInfoSelectedKlantPart2(215);
	}
	
	showOrderButtons(false);
}

function showInfoSelectedKlantPart1()
{
	$('#userName').html(selectedKlant.naam);
	$("#creditInfo").hide();
	$("#userBlockedNotice").hide();
}

var showLoadedKlantDataTimeout;
function showInfoSelectedKlantPart2(timeout)
{
	var start = new Date().getTime();

	$.getJSON('klant/tegoed?klantId='+ selectedKlant.id +'&callback=?', function(data) {
		var elapsed = new Date().getTime() - start;
		var stillToWait = timeout - elapsed;
		if(stillToWait <=0)
		{
			console.log("I have to wait "+ stillToWait +"! Let's do it right now!");
			showLoadedKlantData(data);
		}
		else
		{
			console.log("I have to wait "+ stillToWait +"! Let's do it later!");
			clearTimeout(showLoadedKlantDataTimeout);
			showLoadedKlantDataTimeout = setTimeout(function(){
				showLoadedKlantData(data);
			}, stillToWait);
		}
	});
}

function showLoadedKlantData(data)
{
	$("#credit").html(formatter.format(data.tegoed));
	$("#creditInfo").show();
	
	if(data.geblokkeerd)
	{
		$(".productSelectButton").attr('disabled','disabled');
		$('#userBlockedNotice').show();
	}
	else
	{
		$(".productSelectButton").removeAttr('disabled');
	}
	
	selectedKlantGeblokkeerd = data.geblokkeerd
	showOrderButtons(false);
}

function selectProduct(clickedButton, product)
{
	$("#order").removeClass("manualTransition");
	$("#order").removeClass("orderComplete");
	
	var found = false;
	for(var i = 0; i < order.length; i++)
	{
		var productInOrder = order[i][0];
		if(productInOrder == product)
		{
			order[i][1]++;
			found = true;
			break;
		}
	}
	if(!found)
	{
		order.push([product, 1]);
	}

	redrawOrder();
}

function redrawOrder()
{
	$('.reduceOrder').off('click');
	$('#order').empty();
	for(var i = 0; i < order.length; i++)
	{
		var productId = order[i][0].id;
		var productNaam = order[i][0].naam;
		var count = order[i][1];
		var productPrijs = (count * order[i][0].prijs).toFixed(2);
		var formattedProductPrijs = formatter.format(productPrijs);
		var orderLine = $('<div class="orderLine"><span class="orderNumber">'+count+'</span>'+productNaam +' <div id="reduce'+ productId +'" class="reduceOrder"></div><span class="orderPrice">&euro;'+ formattedProductPrijs +'</span></div>');
		$('#order').append(orderLine);
		
		$("#reduce"+ productId).click({productId: productId}, reduce);
	}
	
	showOrderButtons(false);
}

function reduce(event)
{
	var productId = event.data.productId; 
	
	var newOrder = [];
	
	for(var i = 0; i < order.length; i++)
	{
		var productInOrder = order[i][0];
		if(productInOrder.id == productId)
		{
			order[i][1]--;
		}
		
		if(order[i][1] != 0)
		{
			newOrder.push(order[i]);
		}
	}
	
	order = newOrder;
	
	redrawOrder();
}

function showOrderButtons(ranASecondTime)
{
	if(selectedKlant == null && order.length == 0)
	{
		// No order
		if(!ranASecondTime)
		{
			$("#orderActionsArea").removeClass("orderActionsAreaShown");
			setTimeout(function(){
				showOrderButtons(true);
			}, 200);
		}
		else
		{
			// set the clearOrderDiv to the state of an incomplete order for the next time
			// I could disable the CSS transitions in specific cases, but I'm too lazy to do that.
			// Also, the current code causes some weird rendering when switching from a blocked user to a blocked user, 
			// but I don't really care about that either
			$("#clearOrderDiv").removeClass("completeClearOrderDiv");
		}
	}
	else if(selectedKlantGeblokkeerd == true || selectedKlant == null && order.length != 0 || selectedKlant != 0 && order.length == 0)
	{
		// incomplete order
		$("#orderActionsArea").addClass("orderActionsAreaShown");
		$("#clearOrderDiv").removeClass("completeClearOrderDiv");
	}
	else
	{
		// complete order
		$("#orderActionsArea").addClass("orderActionsAreaShown");
		$("#clearOrderDiv").addClass("completeClearOrderDiv");
	}
}

function clearOrder()
{
	selectedKlant = null;
	selectedKlantGeblokkeerd = false;
	$('.userSelectButton').removeClass('selected');
	order = [];
	$(".productSelectButton").removeAttr('disabled');
	$("#order").addClass("manualTransition");
	$("#order").addClass("orderComplete");
	
	showOrderButtons(false);
}

function clearSelectedKlantText()
{
	$("#orderArea").removeClass("orderAreaVisible");
}


JSON.stringify = JSON.stringify || function (obj) {
    var t = typeof (obj);
    if (t != "object" || obj === null) {
        // simple data type
        if (t == "string") obj = '"'+obj+'"';
        return String(obj);
    }
    else {
        // recurse array or object
        var n, v, json = [], arr = (obj && obj.constructor == Array);
        for (n in obj) {
            v = obj[n]; t = typeof(v);
            if (t == "string") v = '"'+v+'"';
            else if (t == "object" && v !== null) v = JSON.stringify(v);
            json.push((arr ? "" : '"' + n + '":') + String(v));
        }
        return (arr ? "[" : "{") + String(json) + (arr ? "]" : "}");
    }
};

var naOrderKlantWegTimeout;
var postingOrder = false;
function confirmOrder()
{
	// selectedKlant could be null while clearOrder is being executed (and the confirm order is being hidden), 
	// the user quickly clicks on the confirm order button which clearly doesn't have any use anymore (which is why it is being hidden) 
	if(!postingOrder && selectedKlant != null) 
	{
		postingOrder = true;
		
		$.post("bestelling/bevestigBestelling", {klantId: selectedKlant.id, order:JSON.stringify(order)}, function(data) {
			var userdata = $.parseJSON(data);
			var message = userdata.message;
			if(message)
			{
				alert(message);
			}
			$("#credit").html(userdata.tegoed.toFixed(2));
			
			clearOrder();
			clearTimeout(naOrderKlantWegTimeout);
			naOrderKlantWegTimeout = setTimeout(function(){
				clearSelectedKlantText();
			},1500);
			
			postingOrder = false;
		});
	}

}

function customerUpdated(updatedCustomerId)
{
	if(selectedKlant != null && updatedCustomerId == selectedKlant.id)
	{
		showInfoSelectedKlantPart2(0);
	}
}

