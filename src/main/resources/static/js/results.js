var ori_list; //the list of searched items
var ori_item;
var search_ind; //index of item chosen in the search list
let alt_list; //the list of alternate items
let item; //item chosen in the search list
var confirm_page = false; //if we arrived at the confirmed page
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
//cur to mark current location
//searched to mark searched item
function clearMap(cur,searched,skip){
  document.getElementById("map").style.display="block";
  if (!skip){
  	clearRoute();
  }
  clearMarkers();
  if (cur){
    createMarker(pos.lat, pos.lng, "",true,"Your current location","blue");
  } 
  if (searched){
    createMarker(ori_item.lat, ori_item.lon, "0",true,ori_item.name+" $"+ori_item.price,"green");
  }
}

function clearConfirm(){
  document.getElementById("confirm_button").style.display="none";
  document.getElementById("back_button").style.display="none";
  document.getElementById("confirm_mesg").innerHTML="";
  document.getElementById("map").style.display="none";
}

function clearSearch(){
  document.getElementById('results').innerHTML="";
  document.getElementById("map").style.display="none";
}

function clearAlt(){
  document.getElementById("alt_name").innerHTML="";
  document.getElementById("switches").style.display="none";
  document.getElementById('alt_results').innerHTML="";
  document.getElementById("map").style.display="none";
  document.getElementById("filters").style.display="none";
}

function clearHistTable(){
  document.getElementById('history_table').innerHTML="";
}

//generates a list of buttons based on a parsed json list of items
function generate_searched(items) {
  confirm_page = false;
  clearConfirm();
  clearAlt();
  clearHistTable();
  clearMap(true,false,false);
  results=document.getElementById('results');
  ori_list=items;
  let n=Math.min(items.length,200);
  let bt;
  let i;
  
  if (n == 0) {
    alert("No item found. Please try another item.");
  	return;
  }
  //results.innerHTML='';
  var x = '<div class="row">'+
'	    <div class="col-12">'+
'	      <h6 class="text-muted">Please Select An Item Below:</h6> '+
'	      '+
'	      <ul class="list-group ">';
  var overlap_start = 0;
  for (i =0; i<n-1; ++i) {
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


    if (items[i].lat != items[i+1].lat || items[i].lon!=items[i+1].lon){
    	if (i!=overlap_start){
    		createMarker(items[i].lat, items[i].lon, (overlap_start+1)+"-"+(i+1),false,"Multiple item: No."+(overlap_start+1)+"-No."+(i+1)+" @"+items[i].store,"red");
    	}else{
    		createMarker(items[i].lat, items[i].lon, (i+1).toString(),false,items[i].name+" $"+items[i].price,"red");
    	}
    	overlap_start = i+1;
    }
    
  }
  results.innerHTML = x +
'	      </ul>'+
'	    </div>'+
'	  </div>';

}

//generates redirect to correct state
function redirect_state(task) {
    var lat = task.userLat;
    var lon = task.userLon;
    var date = new Date(task.taskStartTime);
	if (task.finalLat != 0.0 && task.finalLon != 0.0){
	   //go to confirm
	   document.getElementById("ongoing").innerHTML='We detected your ongoing task on '+ date.toDateString() +
	   ' at '+ date.toTimeString() + ' Resuming below '+ ' <button type="button" onclick = "reset()" class="btn btn-warning">'+
            'Start a new search? </button>';
	   
	   overwritePos(lat,lon);
	   document.getElementById("search_bar").value = task.searchString;
	   ori_item = task.initialItem;
	   item = ori_item;
	   ori_list = task.initialItems;
	   alt_list = task.alternativeItem;
	   show_confirm(-2, task.initialItem, task.finalItem,true);
	   drawRoute(task.finalLat,task.finalLon);
	   
	} else if (task.initialLat!=0.0 && task.initialLon!=0.0){
	   // go to request alt
	   
	   document.getElementById("ongoing").innerHTML='We detected your ongoing task on '+ date.toDateString() +
	   ' at '+ date.toTimeString() + ' Resuming below '+ ' <button type="button" onclick = "reset()"  class="btn btn-warning">'+
            'Start a new search? </button>';
	   overwritePos(lat,lon);
	   document.getElementById("search_bar").value = task.searchString;
	   ori_item = task.initialItem;
	   item = ori_item;
	   ori_list = task.initialItems;
	   show_alt(-1, task.initialItem);
	   
	} else if (task.searchString != ""){
	   // init search
	   
	   document.getElementById("ongoing").innerHTML='We detected your ongoing task on '+ date.toDateString() +
	   ' at '+ date.toTimeString() + ' Resuming below '+ ' <button type="button" onclick = "reset()"  class="btn btn-warning">'+
            'Start a new search? </button>';
	   overwritePos(lat,lon);
	   document.getElementById("search_bar").value = task.searchString;
	   init_search(task.searchString);
	}
}
function back_alt(){
  show_alt(-1,ori_item);
}

//switches to the view for alternate items
function show_alt(item_index,item_) {
    if (item_index != -1){
	  	search_ind=item_index;
	  	ori_item = ori_list[item_index];
	  }else {
	    ori_item = item_;	
	    
	  }
  confirm_page = false;
  clearConfirm();
  clearSearch();
  clearHistTable();
  clearMap(true, true,false);
  document.getElementById("filters").style.display="block";

  item = ori_item;
  
  document.getElementById("switches").style.display="block";
  document.getElementById("alt_name").innerHTML="You chose: "+
  '	        <button onclick = "select_alternative('+ori_item.barcode+','+ori_item.lat+', '+ori_item.lon+',-1)" class="list-group-item list-group-item-action d-flex  w-100 justify-content-between justify-content-between align-items-center ">'+
'	          <div class="column" >'+
'	    		<h5>0: '+ori_item.name+'</h5>'+
'	    		<p>Price: $'+ori_item.price+'</p>'+
'	    		<small>Store Name: '+ori_item.store+'</small>'+
'	  		  </div>'+
'	  		  <div class="column" >'+
'	    		<small>'+storeDist(ori_item.lat,ori_item.lon)+' km</small>'+
'	    		<div class="image-parent">'+
'	        		<img src="'+ori_item.image+'" class="img-fluid" alt="ori_item" width="100" height="100">'+
'	     		</div>'+
'	   		  </div>'+
'	  	    </button>'+" <br /> here are some alternatives:";
  request_alternatives(cheaper,closer,same);
}

//generate alternate items
function generate_alt(items) {
  clearMap(true, true,false);

  alt_list=items;
  results=document.getElementById('alt_results');
  let n=Math.min(items.length,200);
  let bt;
  let i;
  var overlap_start = 0;
//  results.innerHTML='';
  var x = '<div class="row">'+
'	    <div class="col-12">'+
'	      '+
'	      <ul class="list-group ">';

  for (i =0; i<n-1; ++i) {
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
    if (items[i].lat != items[i+1].lat || items[i].lon!=items[i+1].lon){
    	if (i!=overlap_start){
    		createMarker(items[i].lat, items[i].lon, (overlap_start+1)+"-"+(i+1),false,"Multiple item: No."+(overlap_start+1)+"-No."+(i+1)+" @"+items[i].store,"red");
    	}else{
    		createMarker(items[i].lat, items[i].lon, (i+1).toString(),false,items[i].name+" $"+items[i].price,"red");
    	}
    	overlap_start = i+1;
    }
  }
  results.innerHTML = x +
'	      </ul>'+
'	    </div>'+
'	  </div>';

createMarker(ori_item.lat, ori_item.lon, "0",true,ori_item.name+" $"+ori_item.price,"green");
}

//use api endpoint to get saved value
function show_confirm(item_index,ori_item_,fin_item_,skip) {
  confirm_page = true;
  document.getElementById("wait_mesg").style.display="none";
  clearAlt();
  clearHistTable();
  clearSearch();
  clearMap(false,false,skip);
  if (item_index == -1){
  	document.getElementById("confirm_mesg").innerHTML="You are purchasing "+ori_item.name+" $"+ori_item.price+" <br /> You have spent $"+parseFloat(ori_item.price).toFixed(2);
    document.getElementById("confirm_button").style.display="block";
    document.getElementById("back_button").style.display="block";
  	drawRoute(ori_item.lat,ori_item.lon);
  } else{
	if (item_index==-2){
	  	ori_item=ori_item_;
    	fin_item=fin_item_;
    } else{
    	fin_item=alt_list[item_index];
    }
  	document.getElementById("confirm_mesg").innerHTML="You are purchasing "+fin_item.name+" $"+fin_item.price+" <br /> You would save $"+parseFloat(Math.max(ori_item.price-fin_item.price,0)).toFixed(2);
    document.getElementById("confirm_button").style.display="block";
    document.getElementById("back_button").style.display="block";
  	drawRoute(fin_item.lat,fin_item.lon);
  }
}

//generate history
function generate_history(records) {
  console.log(records);
  
  confirm_page = false;
  clearConfirm()
  clearAlt();
  clearSearch();
  
  /*alt_list=records;*/
  results=document.getElementById('history_table');
  let n=records.length;
  /*let bt;*/
  let i;

//  results.innerHTML='';
  var x = '<div class="row">'+
'	    <div class="col-12">'+
'	      '+
'	      <ul class="list-group ">';

  for (i =0; i<n; ++i) {
    x = x+ 
'      <div class="list-group-item list-group-item-action d-flex  w-100 justify-content-between justify-content-between align-items-center ">'+
'	    <div class="column" >'+
			"<br />" +
'	    	<p>Date: '+records[i].date+'</p>'+
'			<p>Item: '+records[i].item+'</p>'+
'	    	<p>Price: $'+parseFloat(records[i].price).toFixed(2)+'</p>'+
'	    	<p>Savings from this purchase: $'+parseFloat(records[i].saving).toFixed(2)+'</p>'+
'		</div>' + 
'     </div>';
  }
  results.innerHTML = x +
'	      </ul>'+
'	    </div>'+
'	  </div>';
}

function updateBalance(bal){
  
}
