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
          window.location.href = "alt.html";
        }
      };
      // alert("item=" + searched_item + "&lat=37.7510&lon=-97.8220");
      xhttp.open("POST", "/select_item", true);
      xhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
      xhttp.send('item_number='+item_index);
    // }  
  } 