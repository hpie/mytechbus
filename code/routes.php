<?php

use Slim\Http\Request;
use Slim\Http\Response;

// Routes

//Home page call
$app->get('/', function() {

	$data['welcom'] = 'Welcome to Tech Bus Ticketing';

	echo json_encode($data);

});

// Get ticket booking listing
$app->get('/bookings', function() {

	require_once('db.php');

	$query = "select * from `ticket_bookings`";

	$result = $conn->query($query);

	while ($row = $result->fetch_assoc()){
		$data[] = $row;
	}

	echo json_encode($data);
});

// Get request for vehicletypes
$app->get('/types', function() {

	require_once('db.php');

	$query = "select * from vehicle_types order by vehicle_type";

	$result = $conn->query($query);

	while ($row = $result->fetch_assoc()){
		$data[] = $row;
	}

	echo json_encode($data);
});

// Get request for vehicletypes
$app->get('/device_access', function() {

	require_once('db.php');

	$query = "select * from `vehicle_device_access` order by row_id";

	$result = $conn->query($query);

	while ($row = $result->fetch_assoc()){
		$data[] = $row;
	}

	echo json_encode($data);
});



// Get request for position log
$app->get('/list_positions', function() {

	require_once('db.php');

	echo $query = "select * from `location_log` order by row_id DESC";

	$result = $conn->query($query);

	while ($row = $result->fetch_assoc()) {
		$data[] = $row;
	}

	echo json_encode($data);
});

// List all booking for device
$app->get('/list_booking', function() {

	require_once('db.php');

	$query = "select * from ticket_booking order by row_id DESC";

	$result = $conn->query($query);

	while ($row = $result->fetch_assoc()){
	$data[] = $row;
	}
	echo json_encode($data);

});

// Login functionality
$app->post('/login', function() {

	require_once('db.php');

	//Check if device is registered
	$deive_query = "select * from `vehicle_device_access` WHERE device_imie = '".$_POST['imei']."'";

	$device_result = $conn->query($deive_query);
	
	// Initially success parameter set to zero
	$data['status'] = '0';

	if($device_result->num_rows > 0) {
		
		$query = "select * from `vehicle_device_access` WHERE loginid = '".$_POST['username']."' AND PASSWORD = '".$_POST['password']."' AND device_imie = '".$_POST['imei']."'";

		$result = $conn->query($query);

		if($result->num_rows > 0) {

			while ($row = $result->fetch_assoc()){
				$user_data = $row;
			}
			
			//Change login attempt to zero on successful login
			$login_attempts = 0;
			
			$data['loginid']		= $user_data['loginid'];
			$data['device_imie']	= $user_data['device_imie'];
			$data['route_code']		= $user_data['route_code'];
			$data['todays_date']	= date('Y-m-d');


			$data['status']		= '1';
			$data['message']		= 'Login Successfull';
		} else {
			$data['message'] = 'Wrong username or password';

			while ($row = $device_result->fetch_assoc()){
				$user_data = $row;
			}
			
			// Get initial login attempt count from database
			$login_attempts = ($user_data['device_login_attempts']) ?: 1;

			$todays_date = date('Y-m-d'); 
			
			//remove time from date
			$last_login_attempt  = date('Y-m-d', strtotime($user_data['modified_dt'])); 
			
			// Check if it is not first login attempt
			if($user_data['device_login_attempts'] > 0) {

				// Check if login attempt is from same day
				if(strtotime($todays_date) == strtotime($last_login_attempt)) {
					$login_attempts++;
				} else {
					$login_attempts = 1;
				}
			}
		}
		
		//Update login details
		$sql = "UPDATE `vehicle_device_access` SET `latitude` = '".$_POST['latitude']."',"; 
		
		if($data['status'] == '1') {
		$sql .= "`device_last_login` = '".date('Y-m-d h:i:s')."',";	
		$login_attempts = 0;
		}
		$sql .= "`longitude` = '".$_POST['longitude']."', `altitude` = '".$_POST['altitude']."', `altutude_accuracy` = '".$_POST['altitudeAccuracy']."', `device_login_attempts` = '".$login_attempts."' WHERE `device_imie` = '".$_POST['imei']."'";

		$conn->query($sql);
	} else {
		$data['message'] = 'Device does not exist on system. Please contact Operator!';
	}



	// Insert login attempts log
	$sql = "INSERT INTO `login_attempts` (`username`, `password`, `imei`, `latitude`, `longitude`, `status`) VALUES ('".$_POST['username']."', '".$_POST['password']."', '".$_POST['imei']."', '".$_POST['latitude']."', '".$_POST['longitude']."', '".$data['status']."')";

	$conn->query($sql);

	$data['sql'] = $sql;
 
 echo json_encode($data);
 
});

//Location log of the vehicle
$app->post('/position_log', function() {

	require_once('db.php');

	$sql = "INSERT INTO `location_log` (`vehical_code`, `route_code`, `conductor_code`, `latitude`, `longitude`, `altitude`, `altutude_accuracy`, `heading`, `speed`, `timestamp`) VALUES ('".$_POST['vehicle_code']."', '".$_POST['route_code']."', '".$_POST['conductor_code']."', '".$_POST['latitude']."', '".$_POST['longitude']."', '".$_POST['altitude']."', '".$_POST['altutude_accuracy']."', '".$_POST['heading']."', '".$_POST['speed']."', '".$_POST['timestamp']."')";

	if ($conn->query($sql) === TRUE) {
		$data['status'] = '0';
		$data['message'] = "New record created successfully";
	} else {
		echo "Error: " . $sql . "<br>" . $conn->error;

		$data['status'] = '0';
		$data['message'] = "Error: " . $sql . "<br>" . $conn->error;
	}

	echo json_encode($data);
});

// Get available routes
$app->post('/routes', function() {

	require_once('db.php');

	$query = "select * from master_routes order by route_code";

	$result = $conn->query($query);

	while ($row = $result->fetch_assoc()) {
		$data[] = $row;
	}

	echo json_encode($data);
});

// Get available routes stages
$app->post('/routes_stages', function() {
	//echo "======>>> <pre>"; print_r($_POST); exit;

	require_once('db.php');

	//Get start stages
	$query = "select * from route_fare_matrix order by route_code";

	$result = $conn->query($query);

	$data['success'] = 0;

	if($result->num_rows > 0) {

		$data['success'] = 1;
		$data['route_code'] = 'R001';
		
		$i = 0;
		while ($row = $result->fetch_assoc()) {
			/*
			$data['start_stages'][$row['start_stage_code']] = $row['start_stage_code'];
			$data['end_stages'][$row['start_stage_code']][] = $row['end_stage_code'];

			//$data['end_stages'][$row['start_stage_code']][$row['end_stage_code']]['name'] = $row['end_stage_code'];
			$data['fare_km'][$row['start_stage_code']][$row['end_stage_code']] = $row['fare_km'];
			$data['fare_full'][$row['start_stage_code']][$row['end_stage_code']] = $row['fare_full'];
			$data['fare_half'][$row['start_stage_code']][$row['end_stage_code']] = $row['fare_half'];
			$data['fare_luggage'][$row['start_stage_code']][$row['end_stage_code']] = $row['fare_luggage'];
			*/
			
			if(!in_array($row['start_stage_code'], $data['start_stages'])) {
			$data['start_stages'][$i] = $row['start_stage_code'];
			$i++;
			}
			$data['end_stages'][$row['start_stage_code']][] = $row['end_stage_code'];

			//$data['end_stages'][$row['start_stage_code']][$row['end_stage_code']]['name'] = $row['end_stage_code'];
			$data['fare_km'][$row['start_stage_code']][$row['end_stage_code']] = $row['fare_km'];
			$data['fare_full'][$row['start_stage_code']][$row['end_stage_code']] = $row['fare_full'];
			$data['fare_half'][$row['start_stage_code']][$row['end_stage_code']] = $row['fare_half'];
			$data['fare_luggage'][$row['start_stage_code']][$row['end_stage_code']] = $row['fare_luggage'];

			//$data['start_stages'][$row['start_stage_code']]['position'] = $i;

			
		}
	}

	echo json_encode($data);
});

//Book ticket
$app->post('/book_ticket', function() {

	require_once('db.php');

	$sql = "INSERT INTO `ticket_bookings` (`booking_reference`, `route_code`, `start_stage`, `end_stage`, `fare_full_passengers`, `fare_full_cost`, `fare_half_passengers`, `fare_half_cost`, `fare_luggage`, `fare_luggage_cost`, `total_fare`, `mobile`, `booking_time`, `created_by`) VALUES ('".$_POST['booking_reference']."', '".$_POST['route_code']."', '".$_POST['start_stage']."', '".$_POST['end_stage']."', '".$_POST['fare_full_passengers']."', '".$_POST['fare_full_cost']."', '".$_POST['fare_half_passengers']."', '".$_POST['fare_half_cost']."', '".$_POST['fare_luggage']."', '".$_POST['fare_luggage_cost']."', '".$_POST['total_fare']."', '".$_POST['mobile']."', '".date('Y-m-d h:i:s', strtotime($_POST['booking_time']))."', '".$_POST['created_by']."')";

	if ($conn->query($sql) === TRUE) {
		$data['status'] = '1';
		$data['message'] = "Ticket booked successfully";
	} else {
		echo "Error: " . $sql . "<br>" . $conn->error;

		$data['status'] = '0';
		$data['message'] = "Error: " . $sql . "<br>" . $conn->error;
	}

	echo json_encode($data);

});


//Book ticket called after time inverval and inserts muliple records
$app->post('/book_ticket_call', function() {

	require_once('db.php');
	
	$is_first = 0;

	$sql = "INSERT IGNORE INTO `ticket_bookings` (`booking_reference`, `route_code`, `start_stage`, `end_stage`, `fare_full_passengers`, `fare_full_cost`, `fare_half_passengers`, `fare_half_cost`, `fare_luggage`, `fare_luggage_cost`, `total_fare`, `mobile`, `booking_time`, `created_by`) VALUES ";
	
	$tickets = json_decode('['.$_POST['ticket_data'].']', true);

	foreach($tickets as $ticket) {
		//echo "=====>>>>>>> <pre>"; print_r();

		//$ticket_data = json_decode($ticket, true);

		$sql .= ($is_first++ == 0) ? '': ', ';
		$sql .= "('".$ticket['booking_reference']."', '".$ticket['route_code']."', '".$ticket['start_stage']."', '".$ticket['end_stage']."', '".$ticket['fare_full_passengers']."', '".$ticket['fare_full_cost']."', '".$ticket['fare_half_passengers']."', '".$ticket['fare_half_cost']."', '".$ticket['fare_luggage']."', '".$ticket['fare_luggage_cost']."', '".$ticket['total_fare']."', '".$ticket['mobile']."', '".date('Y-m-d h:i:s', strtotime($ticket['booking_time']))."', '".$ticket['created_by']."')";
	}
	
	//$data['sql'] = $sql;
	//$data['ticket_data'] = tickets;

	if ($conn->query($sql) === TRUE) {
		$data['status'] = '1';
		$data['message'] = "Ticket booked successfully";
	} else {
		echo "Error: " . $sql . "<br>" . $conn->error;

		$data['status'] = '0';
		$data['message'] = "Error: " . $sql . "<br>" . $conn->error;
	}
	
	
	
	echo json_encode($data);

});


//Book ticket called after time inverval and inserts muliple records
$app->post('/book_ticket_call1', function() {

	require_once('db.php');
	
	$data['status'] = '1';
	$data['message'] = $_POST;
	
	echo json_encode($data);

});
