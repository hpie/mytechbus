--
-- Table structure for table `vehicle_operator_devices`
-- This is to store devices approved by the operator. The device will be mapped to the route. 
--

CREATE TABLE IF NOT EXISTS `vehicle_operator_devices` (
  `device_imie` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is Unique IMIE of the device',
  `device_make` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is device manufacturer Nokia, Samsung, LG, MI, Karbon, Motorolla etc.',
  `device_model` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is device manufacturer model Lumia, S7 etc.',
  `device_os` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is device operating system ANDROID, WINDOWS, IOS.',
  `device_osversion` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is device operating system Version 5.1, 6, 7 etc.',
  `device_network` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is device network provider AIRTEL, VODAFONE, JIO etc.',
  `device_number` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is device calling number',
  `device_status` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT 'ACTIVE, INACTIVE, DELETED',
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
-- Indexes for table `vehicle_operator_devices`
--
ALTER TABLE `vehicle_operator_devices`
  ADD PRIMARY KEY (`device_imie`),
  ADD UNIQUE KEY `operator_devices_uk`(`device_number`);

  
--
-- Indexes for table `vehicle_operator_devices`
-- 
ALTER TABLE vehicle_operator_devices
ADD CONSTRAINT FK_operator_devices_operator_id FOREIGN KEY (operator_id) REFERENCES vehicle_operators(row_id);
  
--
-- AUTO_INCREMENT for dumped tables
--
