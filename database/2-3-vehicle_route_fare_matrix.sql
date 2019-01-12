--
-- Table structure for table `route_fare_matrix`
-- This data will be programatically generated
--
CREATE TABLE IF NOT EXISTS `route_fare_matrix` (
  `row_id` bigint(20) NOT NULL,
  `route_code` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Which route_code this stage belongs to',
  `route_start_stage` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'For Here for Printing if needed',
  `route_end_stage` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Kept Here for Printing if needed',
  `start_stage_code` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is Stage Code from route_stages',
  `end_stage_code` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is Stage Code from route_stages',
  `fare_km` int COMMENT 'This is KM from start_stage to end_stage',
  `fare_full` int COMMENT 'Full ticket fare for journey',
  `fare_half` int COMMENT 'Half ticket fare for journey' ,
  `fare_luggage` int COMMENT 'Luggage ticket fare for journey',
  `vehicle_type` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is Bus type Ordinary, Semi-Delux, Delux to be shown on ticket',
  `created_by` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `created_dt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_dt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `route_fare_matrix`
--
ALTER TABLE `route_fare_matrix`
  ADD PRIMARY KEY (`row_id`),
  ADD UNIQUE KEY `matrix_stage_code_uk`(`route_code`, `start_stage_code`, `end_stage_code`, `vehicle_type`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `route_fare_matrix`
--
ALTER TABLE `route_fare_matrix`
  MODIFY `row_id` bigint(20) NOT NULL AUTO_INCREMENT;