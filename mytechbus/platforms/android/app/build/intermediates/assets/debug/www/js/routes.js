//Localhost url
var api_url = 'http://localhost/techbus/techbus/mytechbus/code/';

//Server url
//var api_url = 'http://mytechbus.hpie.in/';

routes = [
  // Async
  {
    path: '/',
    async: function (routeTo, routeFrom, resolve, reject) {
		console.log(routeTo);

		//alert("home redirect Local storage : " + localStorage.getItem('user-token'));

		if(localStorage.getItem('user-token') != '') {
			resolve({
				componentUrl: './pages/dashboard.html'
			});
		} else {
			resolve({
				componentUrl: './pages/login.html'
			});
		}
    }
  },
  {
    path: '/dashboard/',
    url: './pages/dashboard.html',
  },
  {
    path: '/initial-setup/',
    async: function (routeTo, routeFrom, resolve, reject) {
		console.log(routeTo);
     var url = api_url+'routes';
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
			var select_data = '';
			for (var key in result) {
				//alert("==== : " + key);
				var obj = result[key];
				
				select_data += '<option value="' + obj.route_code + '">' + obj.route_code + '</option>';

			}

			resolve(
			  {
			    componentUrl: './pages/dashboard/initial-setup.html',
			  },
			  {
			    context: {
			      select_data: select_data,
			    }
			  }
			);
			//alert(result.message);
		}
	});
    }
  },
  {
		path: '/book-tickets/',
		//componentUrl: './pages/dashboard/book-tickets.html',
		async: function (routeTo, routeFrom, resolve, reject) {

			//alert('hiiiiiiiiiiiiiii');
			//console.log(routeTo);
			var url = api_url+'routes_stages';

			var booking_reference = new Date().valueOf();
			
			// Get assigned route for logged users / device
			var selected_route = localStorage.getItem('route_code');

			var postdata = {};

			var start_stages	= (localStorage.getItem('start_stages') != '') ? localStorage.getItem('start_stages') : '';
			var end_stages		= (localStorage.getItem('end_stages') ? localStorage.getItem('end_stages') : '');
			var fare_km			= (localStorage.getItem('fare_km') ? localStorage.getItem('fare_km') : ''); 
			var fare_full		= (localStorage.getItem('fare_full') ? localStorage.getItem('fare_full') : '');
			var fare_half		= (localStorage.getItem('fare_half') ? localStorage.getItem('fare_half') : '');
			var fare_luggage	= (localStorage.getItem('fare_luggage') ? localStorage.getItem('fare_luggage') : '');

			var from_stages_select= (localStorage.getItem('from_stages_select') ? localStorage.getItem('from_stages_select') : '');
			
			//localStorage.getItem('start_stages')

			//console.log("already set start_stages : " + localStorage.getItem('start_stages'));
			//console.log("already set end_stages : " + end_stages);


			//Check if route stages are locally stored
			var stages_available	= (localStorage.getItem('stages_available') ? localStorage.getItem('stages_available') : 0);
			
			

			postdata.route_code	= selected_route;

			// Check if route statges are already fetched
			//if(localStorage.getItem('route-stages') != '') {

			if(stages_available == 1) {
				/*
				// check if is return journey
				if(localStorage.getItem('is-return') == 1) {					
					from_stages_select	= start_stages;
				} else {
					from_stages_select	= start_stages;
				}
				*/

				resolve(
					{
						componentUrl: './pages/dashboard/book-tickets.html',
					},
					{
						context: {
							from_stages_select	: from_stages_select,
							booking_reference	: booking_reference
						}
					}
				);
			} else {
				// Request route stage data
				app.request({
					url: url,
					data:postdata,
					type:'POST',
					beforeSend:function(){
					},
					success:function(data) {
						var result = JSON.parse(data);

						if(result.success == 1) {
							console.log("start_stages : " + JSON.stringify(result.start_stages));
							console.log("end_stages : " + JSON.stringify(result.end_stages));

							start_stages	= result.start_stages;
							end_stages		= result.end_stages;
							fare_km			= result.fare_km;
							fare_full		= result.fare_full;
							fare_half		= result.fare_half;
							fare_luggage	= result.fare_luggage;	

							localStorage.setItem('stages_available', 1	);
							localStorage.setItem('stages_route_code', result.route_code);

							localStorage.setItem('start_stages', JSON.stringify(result.start_stages));
							localStorage.setItem('end_stages', JSON.stringify(result.end_stages));
							localStorage.setItem('fare_km', JSON.stringify(result.fare_km));
							localStorage.setItem('fare_full', JSON.stringify(result.fare_full));
							localStorage.setItem('fare_half', JSON.stringify(result.fare_half));
							localStorage.setItem('fare_luggage', JSON.stringify(result.fare_luggage));
						} else {
							alert('No stages found');
							localStorage.setItem('stages_available', 0	);
							localStorage.setItem('stages_route_code', '');
						}
						var from_stages_select = '<option value="" disabled selected>Select Start Stage</option>';
						
						// Create start stage dropdown
						if(start_stages != '') {
							for (var key in start_stages) {
								from_stages_select += '<option value="' + key + '">' + start_stages[key] + '</option>';
							}
						}

						localStorage.setItem('from_stages_select', from_stages_select);

						resolve(
							{
								componentUrl: './pages/dashboard/book-tickets.html',
							},
							{
								context: {
									from_stages_select : from_stages_select,
									booking_reference : booking_reference
								}
							}
						);
					}
				});
			}

			
		}
  },
  {
    path: '/route-details/',
    componentUrl: './pages/dashboard/route-details.html',
  },
  {
    path: '/booking-list/',
    componentUrl: './pages/dashboard/booking-list.html',
  },
  {
    path: '/others/',
    url: './pages/dashboard/others.html',
  },


  {
    path: '/home/',
    url: './pages/home.html',
  },
  {
    path: '/about/',
    url: './pages/about.html',
  },
  {
    path: '/catalog/',
    componentUrl: './pages/catalog.html',
  },
  {
    path: '/product/:id/',
    componentUrl: './pages/product.html',
  },
  {
    path: '/settings/',
    url: './pages/settings.html',
  },
  // Page Loaders & Router
  {
    path: '/page-loader-template7/:user/:userId/:posts/:postId/',
    templateUrl: './pages/page-loader-template7.html',
  },
  {
    path: '/page-loader-component/:user/:userId/:posts/:postId/',
    componentUrl: './pages/page-loader-component.html',
  },
  {
    path: '/request-and-load/user/:userId/',
    async: function (routeTo, routeFrom, resolve, reject) {
      // Router instance
      var router = this;

      // App instance
      var app = router.app;

      // Show Preloader
      app.preloader.show();

      // User ID from request
      var userId = routeTo.params.userId;

      // Simulate Ajax Request
      setTimeout(function () {
        // We got user data from request
        var user = {
          firstName: 'Vladimir',
          lastName: 'Kharlampidi',
          about: 'Hello, i am creator of Framework7! Hope you like it!',
          links: [
            {
              title: 'Framework7 Website',
              url: 'http://framework7.io',
            },
            {
              title: 'Framework7 Forum',
              url: 'http://forum.framework7.io',
            },
          ]
        };
        // Hide Preloader
        app.preloader.hide();

        // Resolve route to load page
        resolve(
          {
            componentUrl: './pages/request-and-load.html',
          },
          {
            context: {
              user: user,
            }
          }
        );
      }, 1000);
    },
  },
  {
    path: '/tabs-animated/',
    url: './pages/tabs-animated.html',
  },
  // Default route (404 page). MUST BE THE LAST
  {
    path: '(.*)',
    url: './pages/404.html',
  },
];
