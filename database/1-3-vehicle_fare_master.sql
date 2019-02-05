--
-- Table structure for table `vehicle_fare_master`
-- This is master for per KM fare fixed for a vehicle type
-- Have kept a provision so can vary operator wise
--
CREATE TABLE IF NOT EXISTS `vehicle_fare_master` (
  `row_id` bigint(20) NOT NULL,
  `fare_full` int COMMENT 'Full ticket fare for km',
  `fare_half` int COMMENT 'Half ticket fare for km' ,
  `fare_luggage` int COMMENT 'Luggage ticket fare for km',
  `vehicle_type` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is vehicle_type from vehicle_types Ordinary, Semi-Delux, Delux to be shown on ticket',
  `operator_id` bigint(20) NOT NULL COMMENT 'This is row_id from vehicle_operators, The operator to whom this fare is applicable',
  `created_by` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `created_dt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_dt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `vehicle_fare_master`
--
ALTER TABLE `vehicle_fare_master`
  ADD PRIMARY KEY (`row_id`),
  ADD UNIQUE KEY `vehicle_fare_master_uk`(`fare_km`, `vehicle_type`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `vehicle_fare_master`
--
ALTER TABLE `vehicle_fare_master`
  MODIFY `row_id` bigint(20) NOT NULL AUTO_INCREMENT;