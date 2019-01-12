<?php

use Slim\Http\Request;
use Slim\Http\Response;

// Routes

$app->get('/', function() {
 
$data['welcom'] = 'Welcome to Tech Bus Ticketing';
 
 echo json_encode($data);
 
});

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

$app->post('/position_log', function() {

	//echo "=>>>>>>> <pre>"; print_r($_POST); exit;

	require_once('db.php');

	$sql = "INSERT INTO `location_log` (`vehical_code`, `route_code`, `conductor_code`, `latitude`, `longitude`, `altitude`, `altutude_accuracy`, `heading`, `speed`, `timestamp`) VALUES ('".$_POST['vehicle_code']."', '".$_POST['route_code']."', '".$_POST['conductor_code']."', '".$_POST['`latitude`']."', '".$_POST['longitude']."', '".$_POST['altitude']."', '".$_POST['altutude_accuracy']."', '".$_POST['heading']."', '".$_POST['speed']."', '".$_POST['timestamp']."')";

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
