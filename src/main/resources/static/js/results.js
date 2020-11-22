let items_list;
let search_ind;
let alt_list;
//generates a list of buttons based on a parsed json list of items
function generate_searched(items) {
  //document.getElementById("map").style="display:none";
  document.getElementById("alt_name").innerHTML="";
  results=document.getElementById('results');
  item_list=items;
  let n=items.length;
  let bt;
  let i;

  results.innerHTML='';

  for (i =0; i<n; ++i) {
    bt=document.createElement("button");
    bt.type="button";
    bt.setAttribute("onclick","select_search("+i+")");
    bt.setAttribute("class", "list-group-item list-group-item-action")
    bt.innerHTML = items[i].name+" $"+items[i].price;
    results.appendChild(bt);
  }
}
//switches to the view for alternate items
function show_alt(item_index) {
  item=item_list[item_index];
  search_ind=item_index;
  document.getElementById('results').innerHTML="";
  document.getElementById("alt_name").innerHTML="You chose "+item.name+" $"+item.price+", <br /> here are some alternatives:";
  //document.getElementById("map").style="display:block";
  request_alternatives();
}

function show_confirm(item_index) {
  fin_item=alt_list[item_index];
  ori_item=item_list[search_ind];
  document.getElementById('alt_results').innerHTML="";
  document.getElementById("confirm_mesg").innerHTML="You are purchasing "+fin_item.name+" $"+fin_item.price+" <br /> You will save $"+ori_item.price-fin_item.price;
  confirm_purchase();
}

//generate alternate items
function generate_alt(items) {
  console.log(items);
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
    bt.innerHTML = items[i].name+" $"+items[i].price;
    results.appendChild(bt);
  }
}