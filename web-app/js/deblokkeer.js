var dataLoaded = false;
var animationEnded = false;
var klanten;

function logIn(password)
{
	$("#passwordField").css("border", "2px solid white");

	
	$.getJSON('logIn?password='+ password, function(data) {
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

	$.getJSON('blockedUsers', function(data) {
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
		var zeButton = $('<button/>',
	    {
	        text: klant.naam,
	        click: function () { deblokkeerKlant($(this),klant); }
	    });
	    zeButton.addClass('blockedUserSelectButton');

		$("#front"+klant.id).append(zeButton);
	});
}

function deblokkeerKlant(clickedButton, klant)
{
	$.getJSON('doDeblokkeer?klantId='+ klant.id +'&callback=?', function(data) {
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