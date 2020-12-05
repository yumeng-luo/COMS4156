var pos;
let map;
let bounds;
let infoWindow;
let currentInfoWindow;
let service;
let infoPane;
//don't need get nearby places, just two markers
function initMap() {
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
            zoom: 15
        });
//        bounds.extend(pos);

//        infoWindow.setPosition(pos);
//        infoWindow.setContent('You are here.');
//        infoWindow.open(map);
//        map.setCenter(pos);

        // Call Places Nearby Search on user's location
        createMarker(pos.lat,pos.lng,"current location");
    }, () => {
        // Browser supports geolocation, but user has denied permission
        handleLocationError(true, infoWindow);
    });
    } else {
        // Browser doesn't support geolocation
        handleLocationError(false, infoWindow);
    }
}

// Handle a geolocation error
function handleLocationError(browserHasGeolocation, infoWindow) {
    // Set default location to Sydney, Australia
        pos = {lat: -33.856, lng: 151.215};
        map = new google.maps.Map(document.getElementById('map'), {
        center: pos,
        zoom: 15
    });

    // Display an InfoWindow at the map center
 //   infoWindow.setPosition(pos);
 //   infoWindow.setContent(browserHasGeolocation ?
 //   'Geolocation permissions denied. Using default location.' :
 //   'Error: Your browser doesn\'t support geolocation.');
 //   infoWindow.open(map);
 //   currentInfoWindow = infoWindow;
    createMarker(pos.lat,pos.lng, "using default position");
}

// Set markers at lat lng
function createMarker(lat, lng, name) {
    const loca=new google.maps.LatLng(lat,lng)
    let marker = new google.maps.Marker({
        position: loca,
        map: map,
        title: name
    });
    const infowindow = new google.maps.InfoWindow({
        content: name,
      });
    // Add click listener to each marker
    google.maps.event.addListener(marker, 'click', () => {
        infowindow.open(map, marker);
    });

    // Adjust the map bounds to include the location of this marker
   bounds.extend(loca);
}

function fitBound(){
      	/* Once all the markers have been placed, adjust the bounds of the map to
     * show all the markers within the visible area. */
    map.fitBounds(bounds);
}

// Builds an InfoWindow to display details above the marker
function showDetails(placeResult, marker, status) {
    if (status == google.maps.places.PlacesServiceStatus.OK) {
     	let placeInfowindow = new google.maps.InfoWindow();
      	placeInfowindow.setContent('<div><strong>' + placeResult.name +
          	'</strong><br>' + 'Rating: ' + placeResult.rating + '</div>');
      	placeInfowindow.open(marker.map, marker);
      	currentInfoWindow.close();
      	currentInfoWindow = placeInfowindow;
      	showPanel(placeResult);
    } else {
      	console.log('showDetails failed: ' + status);
    }
}

// DO NOT REMOVE
module.exports = { initMap: initMap, handleLocationError: handleLocationError, 
   getNearbyPlaces: getNearbyPlaces, nearbyCallback: nearbyCallback, createMarker: createMarker, 
   showDetails: showDetails, pos: pos, map: map, bounds: bounds, infoWindow: infoWindow,
   currentInfoWindow: currentInfoWindow, service: service, infoPane: infoPane }
