--
-- Table structure for table `ticket_bookings`
--

CREATE TABLE `ticket_bookings` (
  `row_id` bigint(20) NOT NULL,
  `booking_reference` varchar(255) DEFAULT NOT NULL  COMMENT 'This is Unique Booking Refrence Code, ideally a UUID or IMIE+timestamp'
  `route_code` varchar(20) DEFAULT NULL,
  `start_stage` varchar(20) DEFAULT NULL,
  `end_stage` varchar(20) DEFAULT NULL,
  `fare_full_passengers` int(11) DEFAULT NULL,
  `fare_full_cost` double(10,2) DEFAULT NULL,
  `fare_half_passengers` int(11) DEFAULT NULL,
  `fare_half_cost` double(10,2) DEFAULT NULL,
  `fare_luggage` enum('0','1') DEFAULT '0',
  `fare_luggage_cost` double(10,2) DEFAULT NULL,
  `total_fare` double(10,2) DEFAULT NULL,
  `booking_time` datetime DEFAULT NULL,
  `created_by` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `created_dt` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ticket_bookings`
--
ALTER TABLE `ticket_bookings`
  ADD PRIMARY KEY (`row_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ticket_bookings`
--
ALTER TABLE `ticket_bookings`
  MODIFY `row_id` bigint(20) NOT NULL, AUTO_INCREMENT;
