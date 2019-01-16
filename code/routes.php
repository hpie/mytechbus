<?php

use Slim\Http\Request;
use Slim\Http\Response;

// Routes

$app->get('/', function() {
 
$data['welcom'] = 'Welcome to Tech Bus Ticketing';
 
 echo json_encode($data);
 
});


// Get request for vehicletypes
$app->get('/types', function() {
 
 require_once('db.php');
 
 $query = "select * from vehicle_types order by vehicle_type";
 
 $result = $conn->query($query);
 
 // var_dump($result);
 
 while ($row = $result->fetch_assoc()){
 
$data[] = $row;
 
 }
 
 echo json_encode($data);
 
});


// Get request for position log
$app->get('/list_positions', function() {
 
 require_once('db.php');
 
 $query = "select * from `location_log` order by row_id DESC";
 
 $result = $conn->query($query);
 
 // var_dump($result);
 
 while ($row = $result->fetch_assoc()){
 
$data[] = $row;
 
 }
 
 echo json_encode($data);
 
});

$app->post('/initial_setup', function() {
 
 require_once('db.php');
 
 echo "==>>>>> <pre>"; print_r($_POST); exit;
 
 echo json_encode($data);
 
});

$app->post('/login', function() {
 //echo "======>>> <pre>"; print_r($_POST); exit;
 if($_POST['username'] == "suresh@gmail.com" && $_POST['password'] == "12345") {
	$data['first_name'] = 'Suresh';
	$data['last_name'] = 'Ramsakha';
	$data['user_id'] = 'USER_1';
	$data['token'] = 'xyzabc';
	$data['status'] = '1';
	$data['message'] = 'Login Successfull';

 } else {
	$data['status'] = '0';
	$data['message'] = 'Login failed';
 }
 
 echo json_encode($data);
 
});

//Location log of the vehicle
$app->post('/position_log', function() {

	//echo "=>>>>>>> <pre>"; print_r($_POST); exit;

	require_once('db.php');

	$sql = "INSERT INTO `location_log` (`vehical_code`, `route_code`, `conductor_code`, `latitude`, `longitude`, `altitude`, `altutude_accuracy`, `heading`, `speed`, `timestamp`) VALUES ('".$_POST['vehicle_code']."', '".$_POST['route_code']."', '".$_POST['conductor_code']."', '".$_POST['latitude']."', '".$_POST['longitude']."', '".$_POST['altitude']."', '".$_POST['altutude_accuracy']."', '".$_POST['heading']."', '".$_POST['speed']."', '".$_POST['timestamp']."')";

	//echo "=>>>>>>> <pre>"; print_r($_POST); exit;

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
 //echo "======>>> <pre>"; print_r($_POST); exit;
 
	require_once('db.php');
 
	$query = "select * from master_routes order by route_code";

	$result = $conn->query($query);

	// var_dump($result);

	while ($row = $result->fetch_assoc()){

	$data[] = $row;

	}

	echo json_encode($data);
});

// Get available routes stages
$app->post('/routes_stages', function() {
 //echo "======>>> <pre>"; print_r($_POST); exit;
 
	require_once('db.php');
 
 // gET START STAGES
	$query = "select * from route_fare_matrix order by route_code";

	$result = $conn->query($query);

	// var_dump($result);
	
	$data['success'] = 0;

	if($result->num_rows > 0) {
	
		$data['success'] = 1;
		$data['route_code'] = 'R001';

		while ($row = $result->fetch_assoc()){

			$data['start_stages'][$row['start_stage_code']] = $row['start_stage_code'];
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

	$sql = "INSERT INTO `ticket_booking` (`booking_reference`, `route_code`, `start_stage`, `end_stage`, `fare_full_passengers`, `fare_full_cost`, `fare_half_passengers`, `fare_half_cost`, `fare_luggage`, `fare_luggage_cost`, `total_fare`, `booking_time`, `created_by`) VALUES ('".$_POST['booking_reference']."', '".$_POST['route_code']."', '".$_POST['start_stage']."', '".$_POST['end_stage']."', '".$_POST['fare_full_passengers']."', '".$_POST['fare_full_cost']."', '".$_POST['fare_half_passengers']."', '".$_POST['fare_half_cost']."', '".$_POST['fare_luggage']."', '".$_POST['fare_luggage_cost']."', '".$_POST['total_fare']."', '".date('Y-m-d h:i:s', strtotime($_POST['booking_time']))."', '".$_POST['created_by']."')";

	//echo "=>>>>>>> <pre>"; print_r($_POST); exit;

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
