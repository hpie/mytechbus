--
-- Table structure for table `vehicle_master`
--
CREATE TABLE IF NOT EXISTS `vehicle_master` (
  `row_id` bigint(20) NOT NULL,
  `vehicle_number` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is vehicle registration number',
  `vehicle_make` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'TATA, MAZDA, VOLVO',
  `vehicle_model` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is vehicle model 1210, 1310, 407 etc.',
  `vehicle_registration_date` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Registration year and month',
  `vehicle_engine_no` varchar(20) COLLATE utf8_unicode_ci NULL COMMENT 'Engine Number',
  `vehicle_chassis_no` varchar(20) COLLATE utf8_unicode_ci NULL COMMENT 'Chassis number',
  `vehicle_capacity` int NOT NULL COMMENT 'Passenger Capacity, number of seats',
  `vehicle_type` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'vehicle_type  from vehicle_types table',
  `vehicle_status` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'ACTIVE, INACTIVE, DELETED',
  `operator_id` bigint(20) NOT NULL COMMENT 'This is row_id from vehicle_operators, The operator to whom this vehicle is attached to',
  `created_by` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `created_dt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_dt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `vehicle_master`
--
ALTER TABLE `vehicle_master`
  ADD PRIMARY KEY (`row_id`),
  ADD UNIQUE KEY `vehicle_master_uk`(`vehicle_number`),
  ADD UNIQUE KEY `vehicle_master_engine_uk`(`vehicle_engine_no`),
  ADD UNIQUE KEY `vehicle_master_chassis_uk`(`vehicle_chassis_no`);

  
--
-- Indexes for table `vehicle_master`
-- 
ALTER TABLE vehicle_master
ADD CONSTRAINT FK_vehicle_master_vehicle_type FOREIGN KEY (vehicle_type) REFERENCES vehicle_types(vehicle_type),
ADD CONSTRAINT FK_vehicle_master_operator_id FOREIGN KEY (operator_id) REFERENCES vehicle_operators(row_id);
  
  
--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `vehicle_fare_master`
--
ALTER TABLE `vehicle_master`
  MODIFY `row_id` bigint(20) NOT NULL AUTO_INCREMENT;