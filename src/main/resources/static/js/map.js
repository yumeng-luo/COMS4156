var pos;
let map;
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
            zoom: 15
        });

        directionsRenderer.setMap(map);
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
    // Set default location to campus
        pos = {lat: 40.8106604, lng: -73.9573358};
        map = new google.maps.Map(document.getElementById('map'), {
        center: pos,
        zoom: 15
    });
}

//draws the route from current location to store
//input: destination lat lng
function drawRoute(lat,lng) {
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

// DO NOT REMOVE
module.exports = { initMap: initMap, handleLocationError: handleLocationError, 
   getNearbyPlaces: getNearbyPlaces, nearbyCallback: nearbyCallback, createMarker: createMarker, 
   showDetails: showDetails, pos: pos, map: map, bounds: bounds, infoWindow: infoWindow,
   currentInfoWindow: currentInfoWindow, service: service, infoPane: infoPane }
