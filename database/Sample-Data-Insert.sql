-- Order or insert
-- 1. vehicle_types
-- 2. vehicle_operators
-- 3. vehicle_fare_master
-- 4. vehicle_master
-- 5. master_routes
-- 6. route_stages


-- vehicle_operators
INSERT INTO `mytechbus`.`vehicle_operators` (`row_id`, `operator_name`, `operator_address1`, `operator_address2`, `operator_city`, `operator_state`, `operator_zipcode`, `operator_phone`, `operator_email`, `operator_helpline`, `operator_status`, `created_by`, `created_dt`, `modified_by`, `modified_dt`) VALUES 
(NULL, 'HRTC', 'By Pass', 'Tuttikandi', 'Shimla', 'HP', '171001', '9816098160', 'info@hrtc.in', '9816012345', 'ACTIVE', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP);


-- vehicle_fare_master
INSERT INTO `mytechbus`.`vehicle_fare_master` (`row_id`, `fare_full`, `fare_half`, `fare_luggage`, `vehicle_type`, `operator_id`, `created_by`, `created_dt`, `modified_by`, `modified_dt`) VALUES 
(NULL, '4', '2', '2', 'ORDINARY', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP),
(NULL, '5', '2', '2', 'SEMI-DELUX', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP),
(NULL, '6', '3', '2', 'DELUX', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP),
(NULL, '10', '5', '4', 'VOLVO', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP);



-- vehicle_master
INSERT INTO `mytechbus`.`vehicle_master` (`row_id`, `vehicle_number`, `vehicle_make`, `vehicle_model`, `vehicle_registration_date`, `vehicle_engine_no`, `vehicle_chassis_no`, `vehicle_capacity`, `vehicle_type`, `vehicle_status`, `operator_id`, `created_by`, `created_dt`, `modified_by`, `modified_dt`) VALUES 
(NULL, 'HP03A1111', 'TATA', '1210', '2010-04-01', '11111111', '11111111', '42', 'ORDINARY', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP),
(NULL, 'HP03A2222', 'TATA', '1210', '2011-04-01', '22222222', '22222222', '42', 'ORDINARY', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP),
(NULL, 'HP03A3333', 'TATA', '1213', '2012-04-01', '33333333', '33333333', '52', 'ORDINARY', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP),
(NULL, 'HP03A4444', 'TATA', '1213', '2012-04-01', '44444444', '44444444', '52', 'DELUX', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP),
(NULL, 'HP03A5555', 'VOLVO', '1200C', '2012-04-01', 'VL555555', 'VL555555', '42', 'VOLVO', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP);


-- master_routes
INSERT INTO `mytechbus`.`master_routes` (`row_id`, `route_code`, `route_start_stage`, `route_end_stage`, `route_stop_count`, `route_type`, `route_status`, `operator_id`, `created_by`, `created_dt`, `modified_by`, `modified_dt`) VALUES 
(NULL, 'R-001', 'Shimla', 'Rampur', '', '', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'R-002', 'Shimla', 'Delhi', '', '', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP);
(NULL, 'R-003', 'Rampur', 'Chandigarh', '', '', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP);


-- route_stages
INSERT INTO `mytechbus`.`route_stages` (`row_id`, `stage_code`, `stage_name`, `stage_km`, `stage_no`, `route_code`, `stage_status`,  `operator_id`, `created_by`, `created_dt`, `modified_by`, `modified_dt`) VALUES 
(NULL, 'Shimla', 'SHIMLA', '0', '1', 'R-001', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Fagu', 'FAGU', '15', '2', 'R-001', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Kufri', 'KUFRI', '20', '3', 'R-001', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Theog', 'THEOG', '40', '4', 'R-001', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Maitaina', 'MAITAINA', '50', '5', 'R-001', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Narkenda', 'NARKENDA', '80', '6', 'R-001', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Kumarsain', 'KUMARSAIN', '90', '7', 'R-001', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Sainj', 'SAINJ', '100', '8', 'R-001', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Duttnagar', 'DUTTNAGAR', '120', '9', 'R-001', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Nogli', 'NOGLI', '130', '10', 'R-001', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Rampur', 'RAMPUR', '140', '11', 'R-001', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP);

INSERT INTO `mytechbus`.`route_stages` (`row_id`, `stage_code`, `stage_name`, `stage_km`, `stage_no`, `route_code`, `stage_status`,  `operator_id`,  `created_by`, `created_dt`, `modified_by`, `modified_dt`) VALUES 
(NULL, 'Shimla', 'SHIMLA', '0', '1', 'R-002', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Shoghi', 'SHOGHI', '15', '2', 'R-002', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Knadaghat', 'KNADAGHAT', '30', '3', 'R-002', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Solan', 'SOLAN', '40', '4', 'R-002', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Kasuli', 'KASULI', '50', '5', 'R-002', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Dharampur', 'DHARAMPUR', '60', '6', 'R-002', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Parvanoo', 'PARVANOO', '80', '7', 'R-002', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Kalka', 'KALKA', '90', '8', 'R-002', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Manimajara', 'MANIMAJARA', '95', '9', 'R-002', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Chandigarh', 'CHANDIGARH', '100', '10', 'R-002', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Zirakpur', 'ZIRAKPUR', '115', '11', 'R-002', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP),
(NULL, 'Derabassi', 'DERABASSI', '135', '12', 'R-002', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP),
(NULL, 'Ambala', 'AMBALA', '140', '13', 'R-002', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP),
(NULL, 'Kaithal', 'KAITHAL', '200', '14', 'R-002', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP),
(NULL, 'Peepli', 'PEEPLI', '220', '15', 'R-002', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP),
(NULL, 'Kurukhsetra', 'KURUKHSETRA', '280', '16', 'R-002', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP),
(NULL, 'Azadpur', 'AZADPUR', '320', '17', 'R-002', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP),
(NULL, 'Delhi', 'DELHI', '385', '18', 'R-002', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP);

INSERT INTO `mytechbus`.`route_stages` (`row_id`, `stage_code`, `stage_name`, `stage_km`, `stage_no`, `route_code`, `stage_status`,  `operator_id`, `created_by`, `created_dt`, `modified_by`, `modified_dt`) VALUES 
(NULL, 'Chandigarh', 'CHANDIGARH', '0', '1', 'R-003', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Manimajara', 'MANIMAJARA', '5', '2', 'R-003', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP),
(NULL, 'Kalka', 'KALKA', '30', '3', 'R-003', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Parvanoo', 'PARVANOO', '40', '4', 'R-003', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Dharampur', 'DHARAMPUR', '60', '5', 'R-003', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Kasuli', 'KASULI', '75', '6', 'R-003', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Solan', 'SOLAN', '80', '7', 'R-00#', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Knadaghat', 'KNADAGHAT', '95', '8', 'R-003', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP),
(NULL, 'Shoghi', 'SHOGHI', '100', '8', 'R-003', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Shimla', 'SHIMLA', '110', '10', 'R-003', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Fagu', 'FAGU', '225', '11', 'R-003', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Kufri', 'KUFRI', '220', '12', 'R-003', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Theog', 'THEOG', '240', '13', 'R-003', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Maitaina', 'MAITAINA', '250', '14', 'R-003', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Narkenda', 'NARKENDA', '280', '15', 'R-003', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Kumarsain', 'KUMARSAIN', '290', '16', 'R-003', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Sainj', 'SAINJ', '300', '17', 'R-003', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Duttnagar', 'DUTTNAGAR', '320', '18', 'R-003', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Nogli', 'NOGLI', '330', '19', 'R-003', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP), 
(NULL, 'Rampur', 'RAMPUR', '340', '20', 'R-003', 'ACTIVE', '1', 'SYSTEM', CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP);

-- rout matrix generation logic