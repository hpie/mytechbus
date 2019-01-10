--
-- Table structure for table `route_stages`
--
`route_matrix_count` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
CREATE TABLE IF NOT EXISTS `route_stages` (
  `row_id` bigint(20) NOT NULL,
  `stage_code` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is Unique Stage Code',
  `stage_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `stage_km` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is KM from route_start_stage.',
  `stage_no` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is Stage Sequence from route_start_stage. Matrix will always be calculated from max to min',
  `route_code` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Which route_code this stage belongs to',
  `stage_status` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT 'ACTIVE, INACTIVE, DELETED',
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
  ADD UNIQUE KEY `stage_code_uk`(`stage_code`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `route_stages`
--
ALTER TABLE `route_stages`
  MODIFY `row_id` bigint(20) NOT NULL AUTO_INCREMENT;