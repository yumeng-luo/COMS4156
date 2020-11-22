async function init_search() {
  // if (navigator.geolocation) {
  //   navigator.geolocation.getCurrentPosition(position => {
  //       pos = {
  //           lat: position.coords.latitude,
  //           lng: position.coords.longitude
  //       };
  //     });
      searched_item=document.getElementById("search_bar").value

      const response = await fetch("/search", {
        method: "POST",
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'item='+searched_item+'&lat=37.7510&lon=-97.8220'
      });
      generate_searched(await response.json());
    // }	
  }

function select_search(item_index) {
    console.log(item_index);
    const response = fetch ("/select_item", {
      method: "POST",
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: 'item_number='+item_index
    });
    show_alt(item_index);
} 

function request_alternatives_old(item_index) {
  // searched_item=document.getElementById("search_bar").value

  var xhttp = new XMLHttpRequest(); 
  xhttp.onreadystatechange = function() { 
    if (this.readyState == 4 && this.status == 200) { 
      document.getElementById("alternative-search-result").innerHTML = this.responseText;  
    }
  };
  // alert("item=" + searched_item + "&lat=37.7510&lon=-97.8220");
  xhttp.open("POST", "/alternatives", true);
  xhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
  xhttp.send('lat=37.7510&lon=-97.8220&CHEAPER=false&CLOSER=false&SAME=false');
// }
}

async function request_alternatives() {
  //{
  const response = await fetch ("/alternatives", {
    method: "POST",
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: 'lat=37.7510&lon=-97.8220&CHEAPER=false&CLOSER=false&SAME=false'
  });
  console.log(await response.json());
// }
}

function select_alternative(upc) {
    // searched_item=document.getElementById("search_bar").value
    var xhttp = new XMLHttpRequest(); 
    xhttp.onreadystatechange = function() { 
      if (this.readyState == 4 && this.status == 200) { 
        var Data = JSON.parse(this.responseText);
        localStorage.setItem("final_selection", Data.message);
        window.location.href = "confirm.html";
      }
    };
    // alert("item=" + searched_item + "&lat=37.7510&lon=-97.8220");
    xhttp.open("POST", "/select_purchase", true);
    xhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhttp.send('upc='+upc);
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

