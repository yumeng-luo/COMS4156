function init_search() {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
     document.getElementById("item-search-result").innerHTML = this.responseText;
    }
  };
  xhttp.open("POST", "/search", true);
  xhttp.setRequestHeader("item", "chocolate");
  // xhttp.setRequestHeader("lat", "0.0");
  // xhttp.setRequestHeader("lon", "0.0");
  xhttp.send();
}