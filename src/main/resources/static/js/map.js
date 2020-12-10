var pos;
var map;
var markersArray = [];
let bounds;
let infoWindow;
let currentInfoWindow;
let service;
let infoPane;
var directionsService;
var directionsRenderer;
let result;
//don't need get nearby places, just two markers
function initMap() {
    directionsService = new google.maps.DirectionsService();
    directionsRenderer = new google.maps.DirectionsRenderer();
    bounds = new google.maps.LatLngBounds();
    infoWindow = new google.maps.InfoWindow;
    currentInfoWindow = infoWindow;
    infoPane = document.getElementById('panel');

    if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(position => {
        pos = {
            lat: position.coords.latitude,
            lng: position.coords.longitude
        };
        map = new google.maps.Map(document.getElementById('map'), {
            center: pos,
            zoom: 13
        });
//        directionsRenderer.setMap(map);
    }, () => {
        // Browser supports geolocation, but user has denied permission
        handleLocationError(true, infoWindow);
    });
    } else {
        // Browser doesn't support geolocation
        handleLocationError(false, infoWindow);
    }
    var oms = new OverlappingMarkerSpiderfier(map, {
	  markersWontMove: true,
	  markersWontHide: true,
	  basicFormatEvents: true
	});
}

// Handle a geolocation error
function handleLocationError(browserHasGeolocation, infoWindow) {
    // Set default location to campus
        pos = {lat: 40.8106604, lng: -73.9573358};
        map = new google.maps.Map(document.getElementById('map'), {
        center: pos,
        zoom: 13
    });
//    directionsRenderer.setMap(map);
}

function clearMarkers() {
    for (var i = 0; i < markersArray.length; i++ ) {
        markersArray[i].setMap(null);
      }
      markersArray.length = 0;
}

// Set markers at lat lng
function createMarker(lat, lng, index, info,info_content,color) {
	icon_ = "../image/red-dot.png";

    if (color=="green"){
       icon_ = "../image/green-dot.png";
    }
    else if (color=="blue"){
       icon_ = "../image/blue-dot.png";
    }
    else if (color=="yellow"){
       icon_ = "../image/yellow-dot.png";
    }
    else if (color=="orange"){
       icon_ = "../image/orange-dot.png";
    }
    const loca=new google.maps.LatLng(lat,lng)
    let marker = new google.maps.Marker({
        position: loca,
        map: map,
        //label: index,
        icon: icon_,
    });
    markersArray.push(marker);
    const infowindow = new google.maps.InfoWindow({
        content: info_content,
      });
      if (info){
        infowindow.open(map, marker);
      }
    // Add click listener to each marker
    google.maps.event.addListener(marker, 'click', () => {
        infowindow.open(map, marker);
    });

    // Adjust the map bounds to include the location of this marker
   bounds.extend(loca);
   return marker;
}

function fitBound(){
      	/* Once all the markers have been placed, adjust the bounds of the map to
     * show all the markers within the visible area. */
    map.fitBounds(bounds);
}

//draws the route from current location to store
//input: destination lat lng
function drawRoute(lat,lng) {
    directionsRenderer.setMap(map);
    const start=new google.maps.LatLng(pos.lat,pos.lng);
    const end=new google.maps.LatLng(lat,lng);
    var request = {
        origin: start,
        destination: end,
        travelMode: 'DRIVING'
      };
      directionsService.route(request, function(result, status) {
        if (status == 'OK') {
          directionsRenderer.setDirections(result);
        }
      });
  }
  
function clearRoute(){
    directionsRenderer.setDirections(null);
    directionsRenderer.setMap(null);
}

  
function overwritePos(userlat,userlon){
    pos = {lat: userlat, lng: userlon};
}

// DO NOT REMOVE
module.exports = { initMap: initMap, handleLocationError: handleLocationError, 
   getNearbyPlaces: getNearbyPlaces, nearbyCallback: nearbyCallback, createMarker: createMarker, 
   showDetails: showDetails, pos: pos, map: map, bounds: bounds, infoWindow: infoWindow,
   currentInfoWindow: currentInfoWindow, service: service, infoPane: infoPane ,overwritePos: overwritePos}
