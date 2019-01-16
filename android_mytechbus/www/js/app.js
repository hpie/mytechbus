// Dom7
var $$ = Dom7;


//var logged = localStorage.getItem('user-token');
//$$('a').attr('href', 'http://google.com');

// Framework7 App main instance
var app  = new Framework7({
  root: '#app', // App root element
  id: 'io.framework7.testapp', // App bundle ID
  name: 'Framework7', // App name
  theme: 'auto', // Automatic theme detection
  // App root data
  data: function () {
    return {
      user: {
        firstName: 'John',
        lastName: 'Doe'		
      },
      // Demo products for Catalog section
      products: [
        {
          id: '1',
          title: 'Apple iPhone 8',
          description: 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Nisi tempora similique reiciendis, error nesciunt vero, blanditiis pariatur dolor, minima sed sapiente rerum, dolorem corrupti hic modi praesentium unde saepe perspiciatis.'
        },
        {
          id: '2',
          title: 'Apple iPhone 8 Plus',
          description: 'Velit odit autem modi saepe ratione totam minus, aperiam, labore quia provident temporibus quasi est ut aliquid blanditiis beatae suscipit odio vel! Nostrum porro sunt sint eveniet maiores, dolorem itaque!'
        },
        {
          id: '3',
          title: 'Apple iPhone X',
          description: 'Expedita sequi perferendis quod illum pariatur aliquam, alias laboriosam! Vero blanditiis placeat, mollitia necessitatibus reprehenderit. Labore dolores amet quos, accusamus earum asperiores officiis assumenda optio architecto quia neque, quae eum.'
        },
      ]
    };
  },
  // App root methods
  methods: {
    helloWorld: function () {
      app.dialog.alert('Hello World!');
    },
  },
  // App routes
  routes: routes,
});


// index page 
var mainView = app.views.create('.view-main', {
  url: '/'
});

//-----------------------------

// Handle Cordova Device Ready Event
$$(document).on('deviceready', function() {
   // navigator.geolocation.getCurrentPosition(onSuccess, onError);
});


 // onSuccess Geolocation
//
function onSuccess(position) {

	alert('device is ready');

	var element = document.getElementById('geolocation');
	element.innerHTML = 'Latitude: '           + position.coords.latitude              + '<br />' +
						'Longitude: '          + position.coords.longitude             + '<br />' +
						'Altitude: '           + position.coords.altitude              + '<br />' +
						'Accuracy: '           + position.coords.accuracy              + '<br />' +
						'Altitude Accuracy: '  + position.coords.altitudeAccuracy      + '<br />' +
						'Heading: '            + position.coords.heading               + '<br />' +
						'Speed: '              + position.coords.speed                 + '<br />' +
						'Timestamp: '          + new Date(position.timestamp)          + '<br />';
}

// onError Callback receives a [PositionError](PositionError/positionError.html) object
//
function onError(error) {
	alert('code: '    + error.code    + '\n' +
		  'message: ' + error.message + '\n');
}

function watchPosition() {
   var options = {
      maximumAge: 0,
      timeout: 300000,
      enableHighAccuracy: true,
   }
	
	//commneted to stop geolation call on localhost
   //var watchID = navigator.geolocation.watchPosition(onSuccess, onError, options);

   function onSuccess(position) {
	 
		var url = api_url + 'position_log';
			var postdata = {};

			postdata.vehicle_code		= '';
			postdata.route_code			= 'R001';
			postdata.conductor_code		= '';

			postdata.latitude			= position.coords.latitude;
			postdata.longitude			= position.coords.longitude;
			postdata.altitude			= position.coords.altitude;
			postdata.accuracy			= position.coords.accuracy;
			postdata.altitudeAccuracy	= position.coords.altitudeAccuracy;
			postdata.heading			= position.coords.heading;
			postdata.speed				= position.coords.speed;
			postdata.timestamp			= position.coords.timestamp;

	  app.request({
		url: url,
		data:postdata,
		type:'POST',
		beforeSend:function(){
		},
		success:function(data) {
			var result = JSON.parse(data);
			//alert(result.message);
		}
	});

		
   };

   function onError(error) {
      alert('code: '    + error.code    + '\n' +'message: ' + error.message + '\n');
   }
}

selectUsers = $$('select#routes_select'),

// Initial page setup
$$(document).on('page:init', '.page[data-name="initial-setup1111"]', function (e) {
  // Do something here when page with data-name="about" attribute loaded and initialized

  alert('initial-setup');

  var url = 'http://localhost/techbus/techbus/mytechbus/code/routes';
			var postdata = {};

		postdata.vehicle_code	= '123';
			

	  app.request({
		url: url,
		data:postdata,
		type:'POST',
		beforeSend:function(){
		},
		success:function(data) {
			var result = JSON.parse(data);

			for (var key in result) {
				alert("==== : " + key);
				var obj = result[key];

				
				selectUsers.append('<option value="' + obj.route_code + '">' + obj.route_code + '</option>');

			}
			//alert(result.message);
		}
	});
})
