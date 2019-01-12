<?php
 

 

require 'vendor/autoload.php';
$app = new Slim\App();

/*
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
*/

// Register routes
require __DIR__ . '/routes.php';

$app->run();