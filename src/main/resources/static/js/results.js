let items_list;
//generates a list of buttons based on a parsed json list of items
function generate_searched(items) {
  document.getElementById("alt_name").innerHTML="";
  console.log(items);
  results=document.getElementById('results');
  item_list=items;
  //console.log(items);
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
  document.getElementById('results').innerHTML="";
  document.getElementById("alt_name").innerHTML="You chose "+item.name+" $"+item.price+", <br /> here are some alternatives:";
  request_alternatives();
}

//generate alternate items
function generate_alt(items) {
  console.log(items);
  results=document.getElementById('alt_results');
  let n=items.length;
  let bt;
  let i;

  results.innerHTML='';

  for (i =0; i<n; ++i) {
    bt=document.createElement("button");
    bt.type="button";
    bt.setAttribute("onclick","select_alternative("+items[i].barcode+")");
    bt.setAttribute("class", "list-group-item list-group-item-action")
    bt.innerHTML = items[i].name+" $"+items[i].price;
    results.appendChild(bt);
  }
}