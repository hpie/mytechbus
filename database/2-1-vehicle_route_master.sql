--
-- Table structure for table `master_routes`
--

CREATE TABLE IF NOT EXISTS `master_routes` (
  `row_id` bigint(20) NOT NULL,
  `route_code` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is Unique Route Code',
  `route_start_stage` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `route_end_stage` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `route_stop_count` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This will be updated once route matrix is generated for this route', 
  `route_type` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `route_status` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT 'ACTIVE, INACTIVE, DELETED',
  `operator_id` bigint(20) NOT NULL COMMENT 'The operator to whom this route is attached to',
  `created_by` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `created_dt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_dt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `master_routes`
--
ALTER TABLE `master_routes`
  ADD PRIMARY KEY (`row_id`),
  ADD UNIQUE KEY `route_code_uk`(`route_code`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `vehicle_operators`
--
ALTER TABLE `master_routes`
  MODIFY `row_id` bigint(20) NOT NULL AUTO_INCREMENT;