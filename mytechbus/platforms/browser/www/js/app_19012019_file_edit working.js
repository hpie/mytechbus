// Dom7
var $$ = Dom7;


//var logged = localStorage.getItem('user-token');
//$$('a').attr('href', 'http://google.com');

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
   // navigator.geolocation.getCurrentPosition(onSuccess, onError);
   var deviceID = device.uuid;
   localStorage.setItem('uuid', device.uuid);
  // alert('deviceID : ' + localStorage.getItem('uuid'));

   //alert("= =====: " + cordova.file);

   window.requestFileSystem(window.PERSISTENT, 0, function (fs) {

		alert('file system open: ' + fs.name);
		//createFile(fs.root, "newTempFile.txt", true);

		fs.root.getFile("newTempFile.txt", {create: true, exclusive: false}, function(fileEntry) {

			fileEntry.file(function (file) {
				var reader = new FileReader();

				reader.onloadend = function() {
					alert("Successful file read: " + this.result);
					//alert(fileEntry.fullPath + ": " + this.result);
				};

				reader.readAsText(file);

			}, onErrorReadFile);

		}, onErrorCreateFile);


	}, onErrorLoadFs);

	function createFile(dirEntry, fileName, isAppend) {
		// Creates a new file or returns the file if it already exists.
		dirEntry.getFile(fileName, {create: true, exclusive: false}, function(fileEntry) {

			writeFile(fileEntry, null, isAppend);

		}, onErrorCreateFile);

	}

function writeFile(fileEntry, dataObj, isAppend) {
    // Create a FileWriter object for our FileEntry (log.txt).
    fileEntry.createWriter(function (fileWriter) {
		/*
		fileWriter.onwritestart = function() {
            alert("Successful start write...");
            //readFile(fileEntry);
        };
		*/
        fileWriter.onwriteend = function() {
            alert("Successful file write...");
            readFile(fileEntry);
        };

        fileWriter.onerror = function (e) {
            alert("Failed file write: " + e.toString());
        };

		dataObj = '{name:suresh}';

        // If data object is not passed in,
        // create a new Blob instead.
        if (!dataObj) {
           // dataObj = new Blob(['some file data'], { type: 'text/plain' });
        }
		
		// If we are appending data to file, go to the end of the file.
        if (isAppend) {
            try {
                fileWriter.seek(fileWriter.length);
            }
            catch (e) {
                console.log("file doesn't exist!");
            }
        }
        fileWriter.write('aaaaa');
    });
}


function readFile(fileEntry) {

    fileEntry.file(function (file) {
        var reader = new FileReader();

        reader.onloadend = function() {
            alert("Successful file read: " + this.result);
            //alert(fileEntry.fullPath + ": " + this.result);
        };

        reader.readAsText(file);

    }, onErrorReadFile);
}

function onErrorLoadFs() {
	alert('onErrorLoadFs');
}

function onErrorCreateFile() {
	alert('onErrorCreateFile');
}

function onErrorReadFile() {
	alert('onErrorReadFile');
}

function displayFileData () {
	alert('displayFileData ');
}
	 /*
	alert('hi');
	var type = window.TEMPORARY;
	var size = 5*1024*1024;
	window.requestFileSystem(type, size, successCallback, errorCallback)

	function successCallback(fs) {
		fs.root.getFile('log.txt', {create: true, exclusive: false}, function(fileEntry) {
		alert('File creation successfull!');

		var check_rw = "w";
		if(check_rw == "w"){
			fileEntry.createWriter( gotFileWriter, errorCallback);
		}
		else if(check_rw == "r"){
			fileEntry.file( gotFile, errorCallback);
		}
	}, errorCallback);
	}

	var gotFileWriter = function(writer) {
		var dataToStore = "hello everyone Enter your Text here";
		writer.write(dataToStore);
	};

	var gotFile = function(file){
		var readAsText(file);
	};

	var readAsText = function(file) {
		var reader = new FileReader();
		reader.onloadend = function(evt) {
		   alert("your file reading is completed")
		};
		reader.readAsText(file);
	};

	function errorCallback(error) {
	alert("ERROR: " + error.code)
	}
	*/
	

	/*
	window.requestFileSystem(LocalFileSystem.PERSISTENT, 0,createDirectoryFS,failureInGettingFile)

	var createDirectoryFS = function(fileSystem) {
	   fileSystem.root.getDirectory("Folder_Name", {create: true}, localStorageGetFS);
	};

	var localStorageGetFS = function(dirEntry) {
		alert("Parent Name: " + dirEntry.name);

		
		dirEntry.getFile("File_name", {
			create : true,
			exclusive : false
		}, gotFileEntry, failureInGettingFile);
		
	};

	var failureInGettingFile = function(error) {
		alert(error.code);
	};

	
	

	var gotFileEntry = function(fileEntry) {
		if(check_rw == "w"){
			fileEntry.createWriter( gotFileWriter, failureInGettingFile);
		}
		else if(check_rw == "r"){
			fileEntry.file( gotFile, failureInGettingFile);
		}
	};

	var gotFileWriter = function(writer) {
		var dataToStore = "hello everyone Enter your Text here";
		writer.write(dataToStore);
	};

	var gotFile = function(file){
		var readAsText(file);
	};

	var readAsText = function(file) {
		var reader = new FileReader();
		reader.onloadend = function(evt) {
		   alert("your file reading is completed")
		};
		reader.readAsText(file);
	};

	
	*/
});

// Called after successfull login
function watchPosition() {
   var options = {
      maximumAge: 0,
      timeout: 120000,
      enableHighAccuracy: true,
   }
	
	//commneted to stop geolation call on localhost
   //var watchID = navigator.geolocation.watchPosition(onSuccess, onError, options);

   //var watchID = navigator.geolocation.watchPosition(onSuccess, onError, { timeout: 120000 });

   navigator.geolocation.getCurrentPosition(onSuccess, onError);

   function onSuccess(position) {
		
		//alert('in watch position : ' + position.coords.latitude);
		
		alert('deviceID : ' + localStorage.getItem('uuid'));

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
