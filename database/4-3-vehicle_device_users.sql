--
-- Table structure for table `vehicle_device_users`
-- This is to store users  approved by the operators. who can perform some administrative actions on the device. 
--

CREATE TABLE IF NOT EXISTS `vehicle_device_users` (
  `row_id` bigint(20) NOT NULL,
  `userid` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is userid for the device',
  `password` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is hashed password for the device',
  `device_imie` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is IMIE of the device from which the login request should come',
  `user_last_login` timestamp NOT NULL COMMENT 'Last login timestamp',
  `latitude` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'To capture Last login attempt location on device',
  `longitude` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'To capture Last login attempt location on device',
  `altitude` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'To capture Last login attempt location on device',
  `altutude_accuracy` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'To capture Last login attempt location on device',
  `device_login_attempts` int  NOT NULL COMMENT 'Number of failure login attempts from this device',
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `user_phone` varchar(20) DEFAULT NULL,
  `user_email` varchar(100) NOT NULL,
  `user_status` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT 'ACTIVE, INACTIVE, DELETED',
  `operator_id` bigint(20) NOT NULL COMMENT 'The operator to whom this device user is attached to',
  `created_by` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `created_dt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_dt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `vehicle_device_users`
--
ALTER TABLE `vehicle_device_users`
  ADD PRIMARY KEY (`row_id`),
  ADD UNIQUE KEY `device_users_uk`(`userid`);

  
--
-- Indexes for table `vehicle_device_users`
-- 
ALTER TABLE vehicle_device_users
ADD CONSTRAINT FK_device_users_device_imie_id FOREIGN KEY (device_imie) REFERENCES vehicle_operator_devices(device_imie),
ADD CONSTRAINT FK_device_users_operator_id FOREIGN KEY (operator_id) REFERENCES vehicle_operators(row_id);  
--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `vehicle_device_users`
--
ALTER TABLE `vehicle_device_users`
  MODIFY `row_id` bigint(20) NOT NULL AUTO_INCREMENT;