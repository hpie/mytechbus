// Dom7
var $$ = Dom7;

//Global queue for ticket upload
var ticketsUploadQueue = [];

//Global variable to store new tickets
var ticketsToUpload = [];

//Global flag to check if ticket upload in process
var is_uploading = 0;

// Framework7 App main instance
var app  = new Framework7({
	root	: '#app',					// App root element
	id		: 'com.hpie.mytechbus',	// App bundle ID
	name	: 'My Tech Bus',				// App name
	theme	: 'auto',					// Automatic theme detection
	
	data: function () {  },				// App root data
	
	methods: {
		helloWorld: function () {
			app.dialog.alert('Hello World!');
		},
	},									// App root methods
	
	routes: routes,						// App routes
});


// index page 
var mainView = app.views.create('.view-main', {
  url: '/'
});

//-----------------------------

// Handle Cordova Device Ready Event
$$(document).on('deviceready', function() {
	//Store device id in local storage
	var deviceID = device.uuid;
	localStorage.setItem('uuid', device.uuid);
});

// Called after successfull login
function watchPosition() {
   var options = {
      maximumAge: 0,
      timeout: 3000,
      enableHighAccuracy: true,
   }
	
   //Get current geolocation
   navigator.geolocation.getCurrentPosition(onSuccess, onError);

	function onSuccess(position) {

		alert('deviceID : ' + localStorage.getItem('uuid'));

		var url = api_url + 'position_log';
		var postdata = {};

		postdata.vehicle_code		= '';
		postdata.route_code			= localStorage.getItem('route_code');
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

function errorCallback(error) {
	alert("ERROR: " + error.code)
}

function update_ticket_book(string, data = new Date()) {
	//alert('root function called : ' + string);

	window.requestFileSystem(window.PERSISTENT, 0, function (fs) {

		fs.root.getFile(localStorage.getItem('todays_date') + ".txt", {create: true, exclusive: false}, function(fileEntry) {
			// Code to delete file 
			/*
			fileEntry.remove(function (file) {
                alert("file removed!");
            }, function (error) {
                alert("error occurred: " + error.code);
            }, function () {
                alert("file does not exist");
            });
			*/

			fileEntry.createWriter(function (fileWriter) {
				fileWriter.onwriteend = function() {
					//alert("Successful file write...");
					fileEntry.file(function (file) {
						var reader = new FileReader();

						reader.onloadend = function() {
							console.log(fileEntry.fullPath + " : Successful file read: " + this.result);
						};

						reader.readAsText(file);

					}, errorCallback);
				};

				fileWriter.onerror = function (e) {
					alert("Failed file write: " + e.toString());
				};
				
				// If we are appending data to file, go to the end of the file.
				//alert("file contents" + fileWriter.length);
				fileWriter.seek(fileWriter.length);
				
				var fileRecordSeparator = ",";

				//for first record no separator
				if(fileWriter.length == 0) {
					fileRecordSeparator = "";
				}
				//call file write
				fileWriter.write(fileRecordSeparator + data);
			});
		}, errorCallback);
	}, errorCallback);
}

//call synch function to upload tikets in database
setInterval(function() {
	//console.log("ticketsToUpload : " + ticketsToUpload + " ||||| ticketsUploadQueue : " + ticketsUploadQueue);
	if(is_uploading == 0 && ticketsToUpload.length > 0) {
		uplaod_tikets(ticketsToUpload);
		ticketsToUpload = [];
	} else {
		console.log('is_uploading : ' + is_uploading + '  ||||| ticketsToUpload : ' +ticketsToUpload.length + ' ==== ' + JSON.stringify(ticketsToUpload));
	}
}, 50000); // 2 minutes

//functionality to upload tickets in database
function uplaod_tikets(ticketsToUpload) {
	is_uploading = 1;
	ticketsUploadQueue.push(ticketsToUpload);
	
	var url = api_url+'book_ticket_call';

	// Update record in database
		app.request({
			url: url,
			data: {'ticket_data' : ticketsUploadQueue},
			type:'POST',
			beforeSend:function(){
			//app.showPreloader('Please Wait');
			},
			success:function(data) {
				var result = JSON.parse(data);
				//alert("===== : " + result.status);
				if(result.status =='1')
				{
					//On successful insert empty the ticket queue
					ticketsUploadQueue = [];
				} 
				is_uploading = 0;
			},
			error:function(data) {	
				app.dialog.alert('Error occured');
				is_uploading = 0;
			}
		});
}
