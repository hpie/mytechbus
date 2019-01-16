--
-- Table structure for table `vehicle_portal_users`
-- This will store users who will have access to the backend portal. Will be mapped to the roles. 
--

CREATE TABLE IF NOT EXISTS `vehicle_portal_users` (
  `row_id` bigint(20) NOT NULL,
  `ip_address` varchar(15) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `activation_code` varchar(40) DEFAULT NULL,
  `forgotten_password_code` varchar(40) DEFAULT NULL,
  `forgotten_password_time` timestamp DEFAULT NULL,
  `last_login` timestamp DEFAULT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `user_phone` varchar(20) DEFAULT NULL,
  `user_email` varchar(100) NOT NULL,
  `user_status` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT 'ACTIVE, INACTIVE, DELETED',
  `operator_id` bigint(20) NOT NULL COMMENT 'The operator to whom this portal user is attached to',
  `created_by` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `created_dt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_dt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `vehicle_portal_users`
--
ALTER TABLE `vehicle_portal_users`
  ADD PRIMARY KEY (`row_id`),
  ADD UNIQUE KEY `portal_users_uk`(`username`),
  ADD UNIQUE KEY `portal_users_email_uk`(`user_email`),
  ADD UNIQUE KEY `portal_users_phone_uk`(`user_phone`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `vehicle_portal_users`
--
ALTER TABLE `vehicle_portal_users`
  MODIFY `row_id` bigint(20) NOT NULL AUTO_INCREMENT;