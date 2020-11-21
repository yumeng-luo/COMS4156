//generates a list of buttons based on a parsed json list of items
function generate_list(items) {
    results=document.getElementById('results');
    console.log(items);
    let n=items.length;
    let bt;
    let i;
    let a;

    results.innerHTML='';
  
    for (i =0; i<n; ++i) {
      bt=document.createElement("button");
      bt.type="button";
      bt.setAttribute("onclick","select_search("+i.toString()+")");
      bt.setAttribute("class", "list-group-item list-group-item-action")
      bt.innerHTML = items[i].name+" $"+items[i].price;
      results.appendChild(bt);
    }
  }