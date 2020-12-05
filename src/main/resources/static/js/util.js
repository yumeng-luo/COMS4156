async function init_search() {   
    searched_item=document.getElementById("search_bar").value;
    const response = await fetch("/search", {
      method: "POST",
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: 'item='+searched_item+'&lat='+pos.lat+'&lon='+pos.lng
    });
    generate_searched(await response.json());
  }

function select_search(item_index) {
    const response = fetch ("/select_item", {
      method: "POST",
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: 'item_number='+item_index
    });
    show_alt(item_index);
} 

async function request_alternatives(cheaper,closer,same) {
  //{
  const response = await fetch ("/alternatives", {
    method: "POST",
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: 'lat='+pos.lat+'&lon='+pos.lng+'&CHEAPER='+cheaper+'&CLOSER='+closer+'&SAME='+same
  });
  generate_alt(await response.json());
// }
}

function select_alternative(upc,item_index) {
    // searched_item=document.getElementById("search_bar").value
    var xhttp = new XMLHttpRequest(); 
    xhttp.onreadystatechange = function() { 
      if (this.readyState == 4 && this.status == 200) { 
        var Data = JSON.parse(this.responseText);
        localStorage.setItem("final_selection", Data.message);
      }
    };
    // alert("item=" + searched_item + "&lat=37.7510&lon=-97.8220");
    xhttp.open("POST", "/select_purchase", true);
    xhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhttp.send('upc='+upc);
    show_confirm(item_index);
  // }  
}

function confirm_purchase() {
    // searched_item=document.getElementById("search_bar").value
    var xhttp = new XMLHttpRequest(); 
    xhttp.onreadystatechange = function() { 
      if (this.readyState == 4 && this.status == 200) { 
        // localStorage.setItem("final_selection",this.responseText);
        alert("Success! Great job.")
      }
    };
    // alert("item=" + searched_item + "&lat=37.7510&lon=-97.8220");
    xhttp.open("POST", "/confirm", true);
    xhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhttp.send();
  // }  
} 

// DO NOT REMOVE
module.exports = { init_search: init_search, select_search: select_search, 
   request_alternatives: request_alternatives, select_alternative: select_alternative, 
   confirm_purchase: confirm_purchase}