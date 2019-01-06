--
-- Table structure for table `vehicle_operators`
--

CREATE TABLE IF NOT EXISTS `vehicle_operators` (
  `row_id` bigint(20) NOT NULL,
  `operator_name` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is Bus Operator Name eg HRTC',
  `operator_address1` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `operator_address2` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `operator_city` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `operator_state` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `operator_zipcode` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `operator_phone` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `operator_email` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `operator_helpline` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `created_by` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `created_dt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_dt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `vehicle_operators`
--
ALTER TABLE `vehicle_operators`
  ADD PRIMARY KEY (`row_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `vehicle_operators`
--
ALTER TABLE `vehicle_operators`
  MODIFY `row_id` bigint(20) NOT NULL AUTO_INCREMENT;