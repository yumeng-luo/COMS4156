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
}

function toggle_closer(){
  if (closer){
    closer=false;
  } else {
    closer=true;
  }
}

function toggle_same(){
  if (same){
    same=false;
  } else {
    same=true;
  }
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
  document.getElementById("confirm_mesg").innerHTML="";
  document.getElementById("map").style.display="none";
  document.getElementById("alt_name").innerHTML="";
  results=document.getElementById('results');
  ori_list=items;
  let n=items.length;
  let bt;
  let i;

  results.innerHTML='';

  for (i =0; i<n; ++i) {
    bt=document.createElement("button");
    bt.type="button";
    bt.setAttribute("onclick","select_search("+i+")");
    bt.setAttribute("class", "list-group-item list-group-item-action")
    bt.innerHTML = items[i].name+", $"+items[i].price+", "+storeDist(items[i].lat,items[i].lon)+" km";
    results.appendChild(bt);
  }
}
//switches to the view for alternate items
function show_alt(item_index) {
  item=ori_list[item_index];
  search_ind=item_index;
  document.getElementById('results').innerHTML="";
  document.getElementById("alt_name").innerHTML="You chose "+item.name+", $"+(Math.round(item.price * 100) / 100)+" <br /> here are some alternatives:";
  request_alternatives(cheaper,closer,same);
}

//use api endpoint to get saved value
function show_confirm(item_index) {
  fin_item=alt_list[item_index];
  ori_item=ori_list[search_ind];
  document.getElementById('alt_results').innerHTML="";
  document.getElementById('alt_name').innerHTML="";
  document.getElementById("confirm_mesg").innerHTML="You are purchasing "+fin_item.name+" $"+fin_item.price+" <br /> You haved saved $"+(ori_item.price-fin_item.price);
  document.getElementById("map").style.display="block";
  drawRoute(fin_item.lat,fin_item.lon);
  confirm_purchase();
}

//generate alternate items
function generate_alt(items) {
  alt_list=items;
  results=document.getElementById('alt_results');
  let n=items.length;
  let bt;
  let i;

  results.innerHTML='';

  for (i =0; i<n; ++i) {
    bt=document.createElement("button");
    bt.type="button";
    bt.setAttribute("onclick","select_alternative("+items[i].barcode+","+i+")");
    bt.setAttribute("class", "list-group-item list-group-item-action")
    bt.innerHTML = items[i].name+", $"+items[i].price+", "+storeDist(items[i].lat,items[i].lon)+" km";;
    results.appendChild(bt);
  }
}