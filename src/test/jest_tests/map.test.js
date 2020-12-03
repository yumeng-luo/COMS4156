/**

TODO: migrate to README

The goal of this unit test is to check if appropriate API calls are called by each method.
To run jest tests, simply run `npm run test`` in the directory containing the tests.

Refer to the post by "ajwl" below for setup/trouble-shopting.
https://stackoverflow.com/questions/58613492/how-to-resolve-cannot-use-import-statement-outside-a-module-in-jest

Note: Google Maps Mock used here is largely based on https://github.com/hupe1980/jest-google-maps-mock.
Missing features have been added to be compatible with map.js, and update is in progress.

To use the original mock package (warning: will not work for this test):
import createGoogleMapsMock from 'jest-google-maps-mock';

**/

const GoogleMapsModule = require('../../main/resources/static/js/map');

const createMockFuncsFromArray = (instance, names = []) => {
  names.forEach(name => {
    instance[name] = jest.fn().mockName(name);
  });
};

const createGoogleMapsMock = (libraries = []) => {
  const createMVCObject = instance => {
    const listeners = {};
    instance.listeners = listeners;

    instance.addListener = jest
      .fn((event, fn) => {
        listeners[event] = listeners[event] || [];
        listeners[event].push(fn);
        return {
          remove: () => {
            const index = listeners[event].indexOf(fn);

            if (index !== -1) {
              listeners[event].splice(index, 1);
            }
          },
        };
      })
      .mockName('addListener');

    createMockFuncsFromArray(instance, [
      'bindTo',
      'get',
      'notify',
      'set',
      'setValues',
      'unbind',
      'unbindAll',
    ]);
  };

  const maps = {
    Animation: {
      BOUNCE: 1,
      DROP: 2,
      Lo: 3,
      Go: 4,
    },
    BicyclingLayer: jest.fn().mockImplementation(function() {
      createMVCObject(this);
      createMockFuncsFromArray(this, ['setMap']);
    }),
    Circle: jest.fn().mockImplementation(function(opts) {
      this.opts = opts;
      createMVCObject(this);
      createMockFuncsFromArray(this, [
        'setCenter',
        'setDraggable',
        'setEditable',
        'setMap',
        'setOptions',
        'setRadius',
        'setVisible',
      ]);
    }),
    ControlPosition: {
      TOP_LEFT: 1,
      TOP_CENTER: 2,
      TOP: 2,
      TOP_RIGHT: 3,
      LEFT_CENTER: 4,
      LEFT: 5,
      LEFT_TOP: 5,
      LEFT_BOTTOM: 6,
      RIGHT: 7,
      RIGHT_CENTER: 8,
      RIGHT_BOTTOM: 9,
      BOTTOM_LEFT: 10,
      BOTTOM: 11,
      BOTTOM_CENTER: 11,
      BOTTOM_RIGHT: 12,
      CENTER: 13,
    },
    Data: jest.fn().mockImplementation(function(options) {
      this.options = options;
      createMVCObject(this);
      createMockFuncsFromArray(this, [
        'setControlPosition',
        'setControls',
        'setDrawingMode',
        'setMap',
        'setStyle',
      ]);
    }),
    DirectionsRenderer: jest.fn().mockImplementation(function(opts) {
      this.opts = opts;
      createMVCObject(this);
      createMockFuncsFromArray(this, [
        'setDirections',
        'setMap',
        'setOptions',
        'setPanel',
        'setRouteIndex',
      ]);
    }),
    DirectionsService: function() {},
    DirectionsStatus: {
      INVALID_REQUEST: 'INVALID_REQUEST',
      MAX_WAYPOINTS_EXCEEDED: 'MAX_WAYPOINTS_EXCEEDED',
      NOT_FOUND: 'NOT_FOUND',
      OK: 'OK',
      OVER_QUERY_LIMIT: 'OVER_QUERY_LIMIT',
      REQUEST_DENIED: 'REQUEST_DENIED',
      UNKNOWN_ERROR: 'UNKNOWN_ERROR',
      ZERO_RESULTS: 'ZERO_RESULTS',
    },
    DirectionsTravelMode: {
      BICYCLING: 'BICYCLING',
      DRIVING: 'DRIVING',
      TRANSIT: 'TRANSIT',
      WALKING: 'WALKING',
    },
    DirectionsUnitSystem: {
      IMPERIAL: 1,
      METRIC: 0,
    },
    DistanceMatrixElementStatus: {
      NOT_FOUND: 'NOT_FOUND',
      OK: 'OK',
      ZERO_RESULTS: 'ZERO_RESULTS',
    },
    DistanceMatrixService: function() {},
    DistanceMatrixStatus: {
      INVALID_REQUEST: 'INVALID_REQUEST',
      MAX_DIMENSIONS_EXCEEDED: 'MAX_DIMENSIONS_EXCEEDED',
      MAX_ELEMENTS_EXCEEDED: 'MAX_ELEMENTS_EXCEEDED',
      OK: 'OK',
      OVER_QUERY_LIMIT: 'OVER_QUERY_LIMIT',
      REQUEST_DENIED: 'REQUEST_DENIED',
      UNKNOWN_ERROR: 'UNKNOWN_ERROR',
    },
    ElevationService: function() {},
    ElevationStatus: {
      Co: 'DATA_NOT_AVAILABLE',
      INVALID_REQUEST: 'INVALID_REQUEST',
      OK: 'OK',
      OVER_QUERY_LIMIT: 'OVER_QUERY_LIMIT',
      REQUEST_DENIED: 'REQUEST_DENIED',
      UNKNOWN_ERROR: 'UNKNOWN_ERROR',
    },
    FusionTablesLayer: jest.fn().mockImplementation(function(options) {
      this.options = options;
      createMVCObject(this);
      createMockFuncsFromArray(this, ['setMap', 'setOptions']);
    }),
    Geocoder: function() {},
    GeocoderLocationType: {
      APPROXIMATE: 'APPROXIMATE',
      GEOMETRIC_CENTER: 'GEOMETRIC_CENTER',
      RANGE_INTERPOLATED: 'RANGE_INTERPOLATED',
      ROOFTOP: 'ROOFTOP',
    },
    GeocoderStatus: {
      ERROR: 'ERROR',
      INVALID_REQUEST: 'INVALID_REQUEST',
      OK: 'OK',
      OVER_QUERY_LIMIT: 'OVER_QUERY_LIMIT',
      REQUEST_DENIED: 'REQUEST_DENIED',
      UNKNOWN_ERROR: 'UNKNOWN_ERROR',
      ZERO_RESULTS: 'ZERO_RESULTS',
    },
    GroundOverlay: function() {},
    ImageMapType: function() {},
    InfoWindow: jest.fn().mockImplementation(function() {
      // this.opts = opts;
      createMVCObject(this);
      createMockFuncsFromArray(this, [
        'setMap',
        'setOpacity',
        'setOptions',
        'setPosition',
        'setContent',
        'setShape',
        'setTitle',
        'setVisible',
        'setZIndex',
        'open'
      ]);
    }),
    KmlLayer: function() {},
    KmlLayerStatus: {
      DOCUMENT_NOT_FOUND: 'DOCUMENT_NOT_FOUND',
      DOCUMENT_TOO_LARGE: 'DOCUMENT_TOO_LARGE',
      FETCH_ERROR: 'FETCH_ERROR',
      INVALID_DOCUMENT: 'INVALID_DOCUMENT',
      INVALID_REQUEST: 'INVALID_REQUEST',
      LIMITS_EXCEEDED: 'LIMITS_EXECEEDED',
      OK: 'OK',
      TIMED_OUT: 'TIMED_OUT',
      UNKNOWN: 'UNKNOWN',
    },
    LatLng: function() {},
    LatLngBounds: jest.fn().mockImplementation(function() {
    }),
    MVCArray: function() {},
    MVCObject: jest.fn().mockImplementation(function() {
      createMVCObject(this);
    }),
    Map: jest.fn().mockImplementation(function(mapDiv, opts) {
      this.mapDiv = mapDiv;
      this.opts = opts;
      createMVCObject(this);
      createMockFuncsFromArray(this, [
        'setCenter',
        'setClickableIcons',
        'setHeading',
        'setMapTypeId',
        'setOptions',
        'setStreetView',
        'setTilt',
        'setZoom',
        'fitBounds',
        'getBounds',
        'panToBounds',
      ]);
    }),
    MapTypeControlStyle: {
      DEFAULT: 0,
      DROPDOWN_MENU: 2,
      HORIZONTAL_BAR: 1,
      INSET: 3,
      INSET_LARGE: 4,
    },
    MapTypeId: {
      HYBRID: 'hybrid',
      ROADMAP: 'roadmap',
      SATELLITE: 'satellite',
      TERRAIN: 'terrain',
    },
    MapTypeRegistry: function() {},
    Marker: jest.fn().mockImplementation(function(opts) {
      this.opts = opts;
      createMVCObject(this);
      createMockFuncsFromArray(this, [
        'setMap',
        'setOpacity',
        'setOptions',
        'setPosition',
        'setShape',
        'setTitle',
        'setVisible',
        'setZIndex',
      ]);
    }),
    MarkerImage: function() {},
    MaxZoomService: function() {
      return {
        getMaxZoomAtLatLng: function() {},
      };
    },
    MaxZoomStatus: {
      ERROR: 'ERROR',
      OK: 'OK',
    },
    NavigationControlStyle: {
      ANDROID: 2,
      DEFAULT: 0,
      Mo: 4,
      SMALL: 1,
      ZOOM_PAN: 3,
      ik: 5,
    },
    OverlayView: function() {},
    Point: function() {},
    Polygon: function() {},
    Polyline: function() {},
    Rectangle: function() {},
    SaveWidget: function() {},
    ScaleControlStyle: {
      DEFAULT: 0,
    },
    Size: function() {},
    StreetViewCoverageLayer: function() {},
    StreetViewPanorama: function() {},
    StreetViewPreference: {
      BEST: 'best',
      NEAREST: 'nearest',
    },
    StreetViewService: function() {},
    StreetViewSource: {
      DEFAULT: 'default',
      OUTDOOR: 'outdoor',
    },
    StreetViewStatus: {
      OK: 'OK',
      UNKNOWN_ERROR: 'UNKNOWN_ERROR',
      ZERO_RESULTS: 'ZERO_RESULTS',
    },
    StrokePosition: {
      CENTER: 0,
      INSIDE: 1,
      OUTSIDE: 2,
    },
    StyledMapType: function() {},
    SymbolPath: {
      BACKWARD_CLOSED_ARROW: 3,
      BACKWARD_OPEN_ARROW: 4,
      CIRCLE: 0,
      FORWARD_CLOSED_ARROW: 1,
      FORWARD_OPEN_ARROW: 2,
    },
    TrafficLayer: jest.fn().mockImplementation(function(opts) {
      this.opts = opts;
      createMVCObject(this);
      createMockFuncsFromArray(this, ['setMap', 'setOptions']);
    }),
    TrafficModel: {
      BEST_GUESS: 'bestguess',
      OPTIMISTIC: 'optimistic',
      PESSIMISTIC: 'pessimistic',
    },
    TransitLayer: jest.fn().mockImplementation(function() {
      createMVCObject(this);
      createMockFuncsFromArray(this, ['setMap']);
    }),
    TransitMode: {
      BUS: 'BUS',
      RAIL: 'RAIL',
      SUBWAY: 'SUBWAY',
      TRAIN: 'TRAIN',
      TRAM: 'TRAM',
    },
    TransitRoutePreference: {
      FEWER_TRANSFERS: 'FEWER_TRANSFERS',
      LESS_WALKING: 'LESS_WALKING',
    },
    TravelMode: {
      BICYCLING: 'BICYCLING',
      DRIVING: 'DRIVING',
      TRANSIT: 'TRANSIT',
      WALKING: 'WALKING',
    },
    UnitSystem: {
      IMPERIAL: 1,
      METRIC: 0,
    },
    ZoomControlStyle: {
      DEFAULT: 0,
      LARGE: 2,
      SMALL: 1,
      ik: 3,
    },
    places: {
  	  AutocompleteService: () => {},
  	  PlacesServiceStatus: {
          INVALID_REQUEST: 'INVALID_REQUEST',
  	    NOT_FOUND: 'NOT_FOUND',
          OK: 'OK',
          OVER_QUERY_LIMIT: 'OVER_QUERY_LIMIT',
  	    REQUEST_DENIED: 'REQUEST_DENIED',
          UNKNOWN_ERROR: 'UNKNOWN_ERROR',
  	    ZERO_RESULTS: 'ZERO_RESULTS',
  	  },
  	  setMap: function() {},
  	  PlacesService: jest.fn().mockImplementation(function(mapDiv) {
    		this.mapDiv = mapDiv;
    		createMVCObject(this);
    		createMockFuncsFromArray(this, [
    			'nearbySearch',
    		]);
  	  }),
  	  RankBy: {
  	  	DISTANCE: 'distance',
  	  },
	},
    __gjsload__: function() {},
    event: {
      clearInstanceListeners: jest.fn().mockName('clearInstanceListeners'),
    },
  };

  if (libraries.includes('places')) {
    maps.places = {
      AutocompleteService: jest.fn(() => ({
        getPlacePredictions: jest.fn(),
      })),
    };
  }

  return maps;
};

describe('createGoogleMapsMock', () => {
  let googleMaps;

  beforeEach(() => {
    googleMaps = createGoogleMapsMock();
    global.window.google = googleMaps;
    global.window.google.maps = googleMaps;
  });

  it('mock creation test', () => {
    const mapDiv = document.createElement('div');
    new googleMaps.Map(mapDiv);

    expect(googleMaps.Map).toHaveBeenCalledTimes(1);
    expect(googleMaps.Map.mock.instances.length).toBe(1);
    expect(googleMaps.Map).toHaveBeenLastCalledWith(mapDiv);
  });

  test('test initMap()', () => {
    GoogleMapsModule.initMap();
    expect(google.maps.Map).toHaveBeenCalledTimes(1);
    expect(google.maps.InfoWindow).toHaveBeenCalledTimes(1);
    expect(google.maps.LatLngBounds).toHaveBeenCalledTimes(1);
  });

  test('test handleLocationError(true)', () => {
    let infoWindow = new google.maps.InfoWindow;
    GoogleMapsModule.handleLocationError(true, infoWindow);
    expect(google.maps.InfoWindow).toHaveBeenCalledTimes(1);
  });

  test('test handleLocationError(true)', () => {
    let infoWindow = new google.maps.InfoWindow;
    GoogleMapsModule.handleLocationError(false, infoWindow);
    expect(google.maps.InfoWindow).toHaveBeenCalledTimes(1);
  });

  test('test getNearbyPlaces()', () => {
    GoogleMapsModule.getNearbyPlaces();
    expect(google.maps.places.PlacesService).toHaveBeenCalledTimes(1);
  });

  // test('test nearbyCallback()', () => {
  //   GoogleMapsModule.nearbyCallback();
  //   expect(google.maps.Map).toHaveBeenCalledTimes(1);
  //   expect(google.maps.InfoWindow).toHaveBeenCalledTimes(1);
  //   expect(google.maps.LatLngBounds).toHaveBeenCalledTimes(1);
  // });

  // test('test createMarkers()', () => {
  //   GoogleMapsModule.createMarkers();
  //   expect(google.maps.Map).toHaveBeenCalledTimes(1);
  //   expect(google.maps.InfoWindow).toHaveBeenCalledTimes(1);
  //   expect(google.maps.LatLngBounds).toHaveBeenCalledTimes(1);
  // });

  // test('test showDetails()', () => {
  //   GoogleMapsModule.showDetails();
  //   expect(google.maps.Map).toHaveBeenCalledTimes(1);
  //   expect(google.maps.InfoWindow).toHaveBeenCalledTimes(1);
  //   expect(google.maps.LatLngBounds).toHaveBeenCalledTimes(1);
  // });
});
