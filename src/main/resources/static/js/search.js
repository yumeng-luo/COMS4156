let searched_item;
let pos;

function init_search() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(position => {
        pos = {
            lat: position.coords.latitude,
            lng: position.coords.longitude
        };
      });
      searched_item=document.getElementById("search_bar").value

      var xhttp = new XMLHttpRequest();	
      xhttp.onreadystatechange = function() {	
        if (this.readyState == 4 && this.status == 200) {	
        document.getElementById("searched").innerHTML = this.responseText;	
        }
      };	
      xhttp.open("POST", "/search", true);	
      xhttp.setRequestHeader("item", searched_item);	
      xhttp.setRequestHeader("lat", pos.lat);	
      xhttp.setRequestHeader("lon", pos.lng);	
      xhttp.send();
    }	
  } 