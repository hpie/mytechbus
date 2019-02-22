--
-- Table structure for table `route_stages`
--

CREATE TABLE IF NOT EXISTS `route_stages` (
  `row_id` bigint(20) NOT NULL,
  `stage_code` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is Unique Stage Code',
  `stage_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `stage_km` int NOT NULL COMMENT 'This is KM from route_start_stage.',
  `stage_no` int NOT NULL COMMENT 'This is Stage Sequence from route_start_stage. Matrix will always be calculated from max to min',
  `route_id` bigint(20) NOT NULL COMMENT 'This is row_id from master_routes, Which route_code this stage belongs to',
  `stage_status` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT 'ACTIVE, INACTIVE, DELETED',
  `operator_id` bigint(20) NOT NULL COMMENT 'This is row_id from vehicle_operators, The operator to whom this route stages are attached to',
  `created_by` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `created_dt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_dt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `route_stages`
--
ALTER TABLE `route_stages`
  ADD PRIMARY KEY (`row_id`),
  ADD UNIQUE KEY `route_stage_no_uk`(`operator_id`,`route_id`,`stage_no`),
  ADD UNIQUE KEY `route_stage_code_uk`(`route_id`,`stage_code`,`stage_no`);
  

--
-- Indexes for table `route_stages`
-- 
ALTER TABLE route_stages
ADD CONSTRAINT FK_route_stages_route_id FOREIGN KEY (route_id) REFERENCES master_routes(row_id),
ADD CONSTRAINT FK_route_stages_operator_id FOREIGN KEY (operator_id) REFERENCES vehicle_operators(row_id);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `route_stages`
--
ALTER TABLE `route_stages`
  MODIFY `row_id` bigint(20) NOT NULL AUTO_INCREMENT;