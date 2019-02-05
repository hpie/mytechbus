--
-- Table structure for table `vehicle_route_mappings`
-- This is to map vehicles of an operator to a route. 
-- Assign route ot a Vehicle. 
--

CREATE TABLE IF NOT EXISTS `vehicle_route_mappings` (
  `row_id` bigint(20) NOT NULL,
  `vehicle_id` bigint(20) NOT NULL COMMENT 'This is row_id from vehicle_master, The vehicle running on a route',
  `route_id` bigint(20) NOT NULL COMMENT 'This is row_id from master_routes, The route assigend to a vehicle',
  `operator_id` bigint(20) NOT NULL COMMENT 'This is row_id from vehicle_operators, The operator to whom this device is attached to',
  `created_by` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `created_dt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_dt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `vehicle_route_mappings`
--
ALTER TABLE `vehicle_route_mappings`
  ADD PRIMARY KEY (`row_id`),
  ADD UNIQUE KEY `operator_devices_uk`(`device_imie`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `vehicle_operator_devices`
--
ALTER TABLE `vehicle_operator_devices`
  MODIFY `row_id` bigint(20) NOT NULL AUTO_INCREMENT;