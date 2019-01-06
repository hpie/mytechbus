--
-- Table structure for table `vehicle_types`
--

CREATE TABLE IF NOT EXISTS `vehicle_types` (
  `vehicle_type` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'This is Bus type Ordinary, Semi-Delux, Delux',
  `vehicle_desc` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `vehicle_basefare` int NOT NULL COMMENT 'Minimum fare fixed',
  `created_by` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `created_dt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modified_dt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


--
-- table data
--
INSERT INTO `mytechbus`.`vehicle_types` (`vehicle_type`, `vehicle_desc`, `vehicle_basefare`, `created_by`,) VALUES 
('ORDINARY', 'ORDINARY', '5', 'SYSTEM'), 
('SEMI-DELUX', 'SEMI-DELUX', '7', 'SYSTEM'), 
('DELUX', 'DELUX', '7', 'SYSTEM'), 
('AC-DELUX', 'AC-DELUX', '10', 'SYSTEM'), 
('TWOBYTWO', 'TWOBYTWO', '8', 'SYSTEM'),
('AC-TWOBYTWO', 'AC-TWOBYTWO', '8', 'SYSTEM'),
('SLEEPER', 'SLEEPER', '10', 'SYSTEM'),
('AC-SLEEPER', 'AC-SLEEPER', '12', 'SYSTEM'),
('VOLVO', 'VOLVO', '15', 'SYSTEM');

--
-- Indexes for dumped tables
--


--
-- Indexes for table `vehicle_types`
--
ALTER TABLE `vehicle_types`
  ADD PRIMARY KEY (`vehicle_type`);
  

  
