let pos;
let map;
let bounds;
let infoWindow;
let currentInfoWindow;
let service;
let infoPane;
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
        bounds.extend(pos);

        infoWindow.setPosition(pos);
        infoWindow.setContent('Location found.');
        infoWindow.open(map);
        map.setCenter(pos);

        // Call Places Nearby Search on user's location
        getNearbyPlaces(pos);
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
    infoWindow.setPosition(pos);
    infoWindow.setContent(browserHasGeolocation ?
    'Geolocation permissions denied. Using default location.' :
    'Error: Your browser doesn\'t support geolocation.');
    infoWindow.open(map);
    currentInfoWindow = infoWindow;

    // Call Places Nearby Search on the default location
    getNearbyPlaces(pos);
}
function getNearbyPlaces(position) {
    let request = {
        location: position,
        rankBy: google.maps.places.RankBy.DISTANCE,
        keyword: 'WalMart Target'
    };

    service = new google.maps.places.PlacesService(map);
    service.nearbySearch(request, nearbyCallback);
}

// Handle the results (up to 20) of the Nearby Search
function nearbyCallback(results, status) {
    if (status == google.maps.places.PlacesServiceStatus.OK) {
      	createMarkers(results);
      	// output status
      	results.forEach(place => {
    	var paragraph = document.getElementById("list");
        	paragraph.innerHTML += place.name;
        	paragraph.innerHTML += "<br />"
      	})
    }
}

// Set markers at the location of each place result
	function createMarkers(places) {
  	places.forEach(place => {
        let marker = new google.maps.Marker({
           	position: place.geometry.location,
            map: map,
            title: place.name
        });

        // Add click listener to each marker
        google.maps.event.addListener(marker, 'click', () => {
            let request = {
            placeId: place.place_id,
            fields: ['name', 'formatted_address', 'geometry', 'rating',
                'website', 'photos']
            };

            /* Only fetch the details of a place when the user clicks on a marker.
            * If we fetch the details for all place results as soon as we get
            * the search response, we will hit API rate limits. */
            service.getDetails(request, (placeResult, status) => {
            showDetails(placeResult, marker, status)
            });
    	});

    	// Adjust the map bounds to include the location of this marker
      	bounds.extend(place.geometry.location);
      	});
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

//module.exports = { initMap: initMap, handleLocationError: handleLocationError, 
//    getNearbyPlaces: getNearbyPlaces, nearbyCallback: nearbyCallback, createMarkers: createMarkers, 
//    showDetails: showDetails, pos: pos, map: map, bounds: bounds, infoWindow: infoWindow,
//    currentInfoWindow: currentInfoWindow, service: service, infoPane: infoPane }
