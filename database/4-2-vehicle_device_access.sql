--
-- Table structure for table `vehicle_device_access`
-- This is to store devices approved by the operator. The device will be mapped to the route. 
--

CREATE TABLE IF NOT EXISTS `vehicle_device_access` (
  `row_id` bigint(20) NOT NULL,
  `loginid` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is loginid for the device',
  `password` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is hashed password for the device',
  `device_imie` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'vehicle_operator_devices.device_imie This is IMIE of the device from which the login request should come',
  `route_id` bigint(20) NOT NULL COMMENT 'This is row_id from master_routes, The route assigend to a vehicle. route_code for matrix to be downloaded',
  `vehicle_id` bigint(20) NOT NULL COMMENT 'To which vehicle this device will book ticket for. FK to vehicle_master.row_id ',
  `device_last_login` timestamp NOT NULL COMMENT 'Last login timestamp',
  `latitude` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'To capture Last login attempt location',
  `longitude` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'To capture Last login attempt location',
  `altitude` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'To capture Last login attempt location',
  `altutude_accuracy` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'To capture Last login attempt location',
  `device_login_attempts` int  NOT NULL COMMENT 'Number of failure login attempts from this device',
  `access_status` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT 'ACTIVE, INACTIVE, DELETED',
  `operator_id` bigint(20) NOT NULL COMMENT 'The operator to whom this device is attached to',
  `created_by` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `created_dt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_dt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `vehicle_device_access`
-- One login to One Device
-- One device to One Route
ALTER TABLE `vehicle_device_access`
  ADD PRIMARY KEY (`row_id`),
  ADD UNIQUE KEY `devices_login_uk`(`loginid`),
  ADD UNIQUE KEY `devices_login_imie_uk`(`device_imie`);

  
--
-- Indexes for table `vehicle_device_access`
-- 
ALTER TABLE vehicle_device_access
ADD CONSTRAINT FK_device_access_device_imie_id FOREIGN KEY (device_imie) REFERENCES vehicle_operator_devices(device_imie),
ADD CONSTRAINT FK_device_access_route_id FOREIGN KEY (route_id) REFERENCES master_routes(row_id),
ADD CONSTRAINT FK_device_access_vehicle_id FOREIGN KEY (vehicle_id) REFERENCES vehicle_master(row_id),
ADD CONSTRAINT FK_device_access_operator_id FOREIGN KEY (operator_id) REFERENCES vehicle_operators(row_id);
  
  
--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `vehicle_device_access`
--
ALTER TABLE `vehicle_device_access`
  MODIFY `row_id` bigint(20) NOT NULL AUTO_INCREMENT;