let ori_list;
let search_ind;
let alt_list;
let cheaper=false;
let closer=false;
let same=false;

function toggle_cheaper(){
  if (cheaper){
    cheaper=false;
  } else {
    cheaper=true;
  }
  request_filter(cheaper,closer,same);
}

function toggle_closer(){
  if (closer){
    closer=false;
  } else {
    closer=true;
  }
  request_filter(cheaper,closer,same);
}

function toggle_same(){
  if (same){
    same=false;
  } else {
    same=true;
  }
  request_filter(cheaper,closer,same);
}

function storeDist(lat,lon){
  lat1=pos.lat;
  lat2=lat;
  lon1=pos.lng;
  lon2=lon;
  if ((lat1 == lat2) && (lon1 == lon2)) {
    return 0;
   } else {
     lat1r=lat1 * Math.PI / 180;
     lat2r=lat2 * Math.PI / 180;
    theta = lon1 - lon2;
    dist = Math.sin(lat1r) * Math.sin(lat2r)
      + Math.cos(lat1r) * Math.cos(lat2r)
        * Math.cos(theta * Math.PI / 180);
    dist = Math.acos(dist);
    dist = dist*180/Math.PI;
    dist = dist * 60 * 1.1515;
    dist = dist * 1.609344;
    return Math.round(dist * 10) / 10;
}
}

//generates a list of buttons based on a parsed json list of items
function generate_searched(items) {
  clearMarkers();
  createMarker(pos.lat, pos.lng, "current location",true);
  document.getElementById("map").style.display="hidden";
  document.getElementById("confirm_mesg").innerHTML="";
  document.getElementById("alt_name").innerHTML="";
  results=document.getElementById('results');
  ori_list=items;
  let n=items.length;
  let bt;
  let i;

  //results.innerHTML='';
  var x = '<div class="row">'+
'	    <div class="col-12">'+
'	      <h6 class="text-muted">Please Select An Item Below:</h6> '+
'	      '+
'	      <ul class="list-group ">';
var element = document.getElementById("myvar");
element.innerHTML='Check here to filter our alternative searches';

  for (i =0; i<n; ++i) {
    x = x+ 
'	        <button onclick = "select_search('+i+')" class="list-group-item list-group-item-action d-flex  w-100 justify-content-between justify-content-between align-items-center ">'+
'	          <div class="column" >'+
'	    		<h5>'+(i+1)+": "+items[i].name+'</h5>'+
'	    		<p>Price: $'+items[i].price+'</p>'+
'	    		<small>Store Name: '+items[i].store+'</small>'+
'	  		  </div>'+
'	  		  <div class="column" >'+
'	    		<small>'+storeDist(items[i].lat,items[i].lon)+' km</small>'+
'	    		<div class="image-parent">'+
'	        		<img src="'+items[i].image+'" class="img-fluid" alt="item_image" width="100" height="100">'+
'	     		</div>'+
'	   		  </div>'+
'	  	    </button>';
    createMarker(items[i].lat, items[i].lon, (i+1).toString(),false);
  }
  fitBound()
  results.innerHTML = x +
'	      </ul>'+
'	    </div>'+
'	  </div>';

}
//switches to the view for alternate items
function show_alt(item_index) {
  item=ori_list[item_index];
  search_ind=item_index;
  var element = document.getElementById("myvar");
element.innerHTML='';
  document.getElementById('results').innerHTML="";
  document.getElementById("alt_name").innerHTML="You chose: "+
  '	        <button onclick = "select_alternative('+item.barcode+','+item.lat+', '+item.lon+',-1)" class="list-group-item list-group-item-action d-flex  w-100 justify-content-between justify-content-between align-items-center ">'+
'	          <div class="column" >'+
'	    		<h5>'+item.name+'</h5>'+
'	    		<p>Price: $'+item.price+'</p>'+
'	    		<small>Store Name: '+item.store+'</small>'+
'	  		  </div>'+
'	  		  <div class="column" >'+
'	    		<small>'+storeDist(item.lat,item.lon)+' km</small>'+
'	    		<div class="image-parent">'+
'	        		<img src="'+item.image+'" class="img-fluid" alt="item_image" width="100" height="100">'+
'	     		</div>'+
'	   		  </div>'+
'	  	    </button>'+" <br /> here are some alternatives:";
  request_alternatives(cheaper,closer,same);
}

//use api endpoint to get saved value
function show_confirm(item_index) {
	var switches = document.getElementById("switches");
  document.getElementById("map").style.display="block";
	switches.innerHTML = '';
	
  ori_item=ori_list[search_ind];
  document.getElementById('alt_results').innerHTML="";
  document.getElementById('alt_name').innerHTML="";
  if (item_index == -1){
  	document.getElementById("confirm_mesg").innerHTML="You are purchasing "+ori_item.name+" $"+ori_item.price+" <br /> You haved spent $"+ori_item.price;
    document.getElementById("confirm_button").style.display="block";
  	drawRoute(ori_item.lat,ori_item.lon);
  } else{
  	fin_item=alt_list[item_index];
  	document.getElementById("confirm_mesg").innerHTML="You are purchasing "+fin_item.name+" $"+fin_item.price+" <br /> You haved saved $"+Math.max(ori_item.price-fin_item.price,0);
    document.getElementById("confirm_button").style.display="block";
  	drawRoute(fin_item.lat,fin_item.lon);
  }
}

//generate alternate items
function generate_alt(items) {
  clearMarkers();
  createMarker(pos.lat, pos.lng, "current location",true);
  alt_list=items;
  results=document.getElementById('alt_results');
  let n=items.length;
  let bt;
  let i;

//  results.innerHTML='';
  var x = '<div class="row">'+
'	    <div class="col-12">'+
'	      '+
'	      <ul class="list-group ">';

  for (i =0; i<n; ++i) {
    x = x+ 
'	        <button onclick = "select_alternative('+items[i].barcode+','+items[i].lat+', '+items[i].lon+','+i+')" class="list-group-item list-group-item-action d-flex  w-100 justify-content-between justify-content-between align-items-center ">'+
'	          <div class="column" >'+
'	    		<h5>'+(i+1)+": "+items[i].name+'</h5>'+
'	    		<p>Price: $'+items[i].price+'</p>'+
'	    		<small>Store Name: '+items[i].store+'</small>'+
'	  		  </div>'+
'	  		  <div class="column" >'+
'	    		<small>'+storeDist(items[i].lat,items[i].lon)+' km</small>'+
'	    		<div class="image-parent">'+
'	        		<img src="'+items[i].image+'" class="img-fluid" alt="item_image" width="100" height="100">'+
'	     		</div>'+
'	   		  </div>'+
'	  	    </button>';
    createMarker(items[i].lat, items[i].lon, (i+1).toString(),false);
  }
  fitBound()
  results.innerHTML = x +
'	      </ul>'+
'	    </div>'+
'	  </div>';

}