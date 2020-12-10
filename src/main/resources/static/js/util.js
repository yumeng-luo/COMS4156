async function init_search(searched_item) {
    document.getElementById("wait_mesg").style.display="block";
    if (searched_item==''){
    	searched_item=document.getElementById("search_bar").value;
    }
    const response = await fetch("/search", {
      method: "POST",
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: 'item='+searched_item+'&lat='+pos.lat+'&lon='+pos.lng
    });
    generate_searched(await response.json());
    document.getElementById("wait_mesg").style.display="none";
  }

function select_search(item_index) {
    const response = fetch ("/select_item", {
      method: "POST",
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: 'item_number='+item_index
    });
    show_alt(item_index,"");
} 

async function request_alternatives() {
  document.getElementById("wait_mesg").style.display="block";
  const response = await fetch ("/alternatives", {
    method: "POST",
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  });
  if (!confirm_page){
    generate_alt(await response.json());
  }
  document.getElementById("wait_mesg").style.display="none";
}

async function request_filter(cheaper,closer,same) {
  document.getElementById("wait_mesg").style.display="block";

  const response = await fetch ("/filter", {
    method: "POST",
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: 'lat='+pos.lat+'&lon='+pos.lng+'&CHEAPER='+cheaper+'&CLOSER='+closer+'&SAME='+same
  });
  if (!confirm_page){
    generate_alt(await response.json());
  }
  document.getElementById("wait_mesg").style.display="none";
}

async function retreive_state() {
    document.getElementById("wait_mesg").style.display="block";
    const response = await fetch("/resume", {
      method: "GET",
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    });
    redirect_state(await response.json());
    document.getElementById("wait_mesg").style.display="none";
  }
  
function select_alternative(upc,lat,lon,item_index) {
     const response = fetch ("/select_purchase", {
      method: "POST",
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: 'upc='+upc+'&lat='+lat+'&lon='+lon
    });
    show_confirm(item_index,"","",false);
}

function confirm_purchase() {
    var xhttp = new XMLHttpRequest(); 
    xhttp.onreadystatechange = function() { 
      if (this.readyState == 4 && this.status == 200) { 
        alert("Success! Great job.")
      }
    };
    xhttp.open("POST", "/confirm", true);
    xhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhttp.send();
} 

function send_email() {
	var xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function() {
	    if (xhr.readyState == XMLHttpRequest.DONE) {
	    	var data = JSON.parse(xhr.responseText);
	    	alert(data.message);
	    }
	}
	xhr.open('GET', '/send_email', true);
	xhr.send(null);
} 

async function history() {

  const response = await fetch ("/history", {
    method: "GET",
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  });
  generate_history(await response.json());
}

async function getBalance() {
  const response = await fetch ("/balance", {
    method: "GET",
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  });
  updateBalance(await response.json());
}


// DO NOT REMOVE
module.exports = { init_search: init_search, select_search: select_search, retreive_state: retreive_state,
   request_alternatives: request_alternatives, select_alternative: select_alternative, 
   confirm_purchase: confirm_purchase}