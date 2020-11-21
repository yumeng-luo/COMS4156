let searched_item;
let pos;

function init_search() {
  // if (navigator.geolocation) {
  //   navigator.geolocation.getCurrentPosition(position => {
  //       pos = {
  //           lat: position.coords.latitude,
  //           lng: position.coords.longitude
  //       };
  //     });
      searched_item=document.getElementById("search_bar").value

      var xhttp = new XMLHttpRequest();	
      xhttp.onreadystatechange = function() {	
        if (this.readyState == 4 && this.status == 200) {	
          document.getElementById("searched").innerHTML = this.responseText;	
        }
      };
      // alert("item=" + searched_item + "&lat=37.7510&lon=-97.8220");
      xhttp.open("POST", "/search", true);
      xhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
      xhttp.send('item='+searched_item+'&lat=37.7510&lon=-97.8220');
    // }	
  } 

function select_search(item_index) {
    searched_item=document.getElementById("search_bar").value

    var xhttp = new XMLHttpRequest(); 
    xhttp.onreadystatechange = function() { 
      if (this.readyState == 4 && this.status == 200) {
        alert("redirecting to: " + this.responseText);
        var Data = JSON.parse(this.responseText);
        localStorage.setItem("alternative_item", Data.message);
        window.location.href = "alt.html";
      }
    };
    // alert("item=" + searched_item + "&lat=37.7510&lon=-97.8220");
    xhttp.open("POST", "/select_item", true);
    xhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhttp.send('item_number='+item_index);
  // }  
} 

function request_alternatives(item_index) {
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

