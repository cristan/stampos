var dataLoaded = false;
var animationEnded = false;
var klanten;

//Fancy ECMA-402 number formatting (which uses the default Locale because of the undefined).
//This is supported by Chrome 24, Fx 29 and IE11 (source: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Number/toLocaleString)
var formatter = new Intl.NumberFormat(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2});

function logIn(password)
{
	$("#passwordField").css("border", "2px solid white");

	
	$.getJSON('deblokkeer/logIn?password='+ password, function(data) {
		if(!data.loggedIn)
		{
			$("#passwordField").addClass("incorrectAnimation");
			$("#login").html($("#login").html());// refresh in order to get the animation started
			$('#passwordField').val('');
		}
		else
		{
			$("#login").on('animationend', listener);
			$("#login").on('webkitAnimationEnd', listener);
			$("#login").addClass("fadeoutAnimation");
			
		}
	});

	$.getJSON('deblokkeer/blockedUsers', function(data) {
		klanten = data;
		tekenKlanten();
		dataLoaded = true;
		if(animationEnded)
	    {
		    showContent();
	    }
	});
}

function tekenKlanten()
{
	$("#names").empty();
	var html = "";
	$.each(klanten, function(i, klant)
	{	
		html += 
		'<div class="flip-container" ontouchstart="this.classList.toggle(\'hover\');">'+
		'<div class="flipper" id="flipper'+klant.id+'">'+
		'<div id="front'+ klant.id +'" class="front"></div>'+
		'<div id="back'+ klant.id +'" class="back"></div>'+
		'</div>'+
		'</div>';
	});
	$("#names").html(html);
		
	$.each(klanten, function(i, klant)
	{
		var html = "<strong>"+ klant.naam +"</strong><br/>Rekening: &euro;"+formatter.format(-1 * klant.tegoed)+"<br/>\n"+
			"Laatst betaald: ";
		if(klant.laatstBetaald)
		{
			html += "<abbr class=\"timeago\" title=\""+klant.laatstBetaald +"\">"+new Date(Date.parse(klant.laatstBetaald)).toLocaleDateString()+"</abbr>";
		}
		else
		{
			html += "nooit";
		}
		
		var zeButton = $('<div/>',
	    {
	        html: html,
	        click: function () { deblokkeerKlant($(this),klant); }
	    });
	    zeButton.addClass('blockedUserSelectButton');

		$("#front"+klant.id).append(zeButton);
	});
	$("abbr.timeago").timeago();
}

function deblokkeerKlant(clickedButton, klant)
{
	$.getJSON('deblokkeer/doDeblokkeer?klantId='+ klant.id, function(data) {
		$("#back"+klant.id).html(klant.naam +" is gedeblokkeerd");
		$("#flipper"+klant.id).addClass("flip");
		
	});
}


function listener(e)
{
	// doesn't work in webkit for some reason
	/*switch(e.type) 
	{
	    case "animationstart":
	      break;
	    case "animationend":
		    $("#login").hide();
		    animationEnded = true;
		    if(dataLoaded)
		    {
			    showContent();
		    }
	      break;
	    case "animationiteration":
	      break;
	 }*/
	$("#login").hide();
    animationEnded = true;
    if(dataLoaded)
    {
	    showContent();
    }
}

function showContent()
{
	$("#actions").addClass("fadeinAnimation").show();
}