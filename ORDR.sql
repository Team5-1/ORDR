-- phpMyAdmin SQL Dump
-- version 4.2.10
-- http://www.phpmyadmin.net
--
-- Host: localhost:8889
-- Generation Time: Feb 26, 2015 at 05:16 PM
-- Server version: 5.5.38
-- PHP Version: 5.6.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `ORDR`
--

-- --------------------------------------------------------

--
-- Table structure for table `BasketItems`
--

CREATE TABLE `BasketItems` (
`basket_item_id` bigint(11) unsigned NOT NULL,
  `user_id` bigint(11) unsigned NOT NULL,
  `item_id` bigint(11) unsigned NOT NULL,
  `quantity` smallint(11) unsigned NOT NULL DEFAULT '1'
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `BasketItems`
--

INSERT INTO `BasketItems` (`basket_item_id`, `user_id`, `item_id`, `quantity`) VALUES
(18, 5, 2, 1),
(20, 5, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `Categories`
--

CREATE TABLE `Categories` (
`category_id` bigint(20) unsigned NOT NULL,
  `name` varchar(30) NOT NULL,
  `date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_updated` timestamp NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `Items`
--

CREATE TABLE `Items` (
`item_id` bigint(11) unsigned NOT NULL,
  `date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_updated` timestamp NULL DEFAULT NULL,
  `name` varchar(60) NOT NULL,
  `category_id` bigint(11) unsigned DEFAULT NULL,
  `description` text NOT NULL,
  `price` double(12,2) unsigned NOT NULL,
  `stock_qty` bigint(20) unsigned NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `Items`
--

INSERT INTO `Items` (`item_id`, `date_created`, `date_updated`, `name`, `category_id`, `description`, `price`, `stock_qty`) VALUES
(1, '2015-01-24 15:59:15', '2015-02-09 15:09:38', 'iPhone', NULL, 'The iPhone 6 is the latest iPhone to date', 499.99, 80),
(2, '2015-01-26 11:46:59', NULL, 'Iron Mk 2', NULL, 'Lightweight, cordless', 20.00, 20),
(3, '2015-02-16 12:28:14', NULL, 'MacBook Pro', NULL, 'dsadsadsadsdsadsadsa', 0.00, 0);

--
-- Triggers `Items`
--
DELIMITER //
CREATE TRIGGER `set_date_created` BEFORE INSERT ON `items`
 FOR EACH ROW SET NEW.date_created = NOW()
//
DELIMITER ;
DELIMITER //
CREATE TRIGGER `set_date_updated` BEFORE UPDATE ON `items`
 FOR EACH ROW SET NEW.date_updated = NOW()
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `Users`
--

CREATE TABLE `Users` (
`user_id` bigint(11) unsigned NOT NULL,
  `first_name` varchar(100) NOT NULL DEFAULT '',
  `last_name` varchar(100) NOT NULL DEFAULT '',
  `email` varchar(100) NOT NULL DEFAULT '',
  `password` char(32) NOT NULL DEFAULT '',
  `date_registered` datetime DEFAULT NULL,
  `date_updated` datetime DEFAULT NULL,
  `date_last_logged_in` datetime DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `Users`
--

INSERT INTO `Users` (`user_id`, `first_name`, `last_name`, `email`, `password`, `date_registered`, `date_updated`, `date_last_logged_in`) VALUES
(5, 'Kyle', 'McAlpine', 'k@kylejm.io', '5f4dcc3b5aa765d61d8327deb882cf99', '2015-02-09 14:45:51', '2015-02-13 21:54:35', '2015-02-20 15:43:31'),
(6, 'Dan', 'Aboobakar', 'booby@email.com', '', '2015-02-09 15:58:17', '2015-02-16 12:20:16', NULL),
(9, 'Danyal', 'Aboobakar', 'boby@email.com', '5f4dcc3b5aa765d61d8327deb882cf99', '2015-02-09 16:49:01', NULL, NULL),
(14, 'Tamara', 'Watson', 't@tamara.com', '5f4dcc3b5aa765d61d8327deb882cf99', '2015-02-09 19:06:10', '2015-02-09 19:54:58', NULL),
(15, 'Steve', 'Jobs', 'steve@apple.com', '5f4dcc3b5aa765d61d8327deb882cf99', '2015-02-09 21:02:15', NULL, NULL),
(16, 'Kyle', 'McAlpine', 'kylejmcalpine@gmail.com', 'c54a16ca8fa833f9d23dbba08f617243', '2015-02-09 22:09:52', '2015-02-09 22:57:19', '2015-02-09 22:58:09'),
(19, 'Ryan', 'McAlpine', 'ryan@me.com', 'fdd6feaa9d7bb24e042c0ce6846439b2', '2015-02-19 11:32:55', NULL, NULL),
(29, 'Ali', 'Younas', 'ali@younas.com', '5f4dcc3b5aa765d61d8327deb882cf99', '2015-02-20 15:47:16', NULL, '2015-02-20 15:48:07');

--
-- Triggers `Users`
--
DELIMITER //
CREATE TRIGGER `set_date_registered` BEFORE INSERT ON `users`
 FOR EACH ROW SET NEW.date_registered = NOW()
//
DELIMITER ;
DELIMITER //
CREATE TRIGGER `set_updated_date` BEFORE UPDATE ON `users`
 FOR EACH ROW IF (NEW.date_last_logged_in <=> OLD.date_last_logged_in) THEN
  SET NEW.date_updated = NOW();
END IF
//
DELIMITER ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `BasketItems`
--
ALTER TABLE `BasketItems`
 ADD PRIMARY KEY (`basket_item_id`), ADD UNIQUE KEY `user_item` (`user_id`,`item_id`), ADD KEY `item` (`item_id`);

--
-- Indexes for table `Categories`
--
ALTER TABLE `Categories`
 ADD PRIMARY KEY (`category_id`);

--
-- Indexes for table `Items`
--
ALTER TABLE `Items`
 ADD PRIMARY KEY (`item_id`), ADD KEY `name` (`name`), ADD KEY `category_id` (`category_id`);

--
-- Indexes for table `Users`
--
ALTER TABLE `Users`
 ADD PRIMARY KEY (`user_id`), ADD UNIQUE KEY `email_address` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `BasketItems`
--
ALTER TABLE `BasketItems`
MODIFY `basket_item_id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=21;
--
-- AUTO_INCREMENT for table `Categories`
--
ALTER TABLE `Categories`
MODIFY `category_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Items`
--
ALTER TABLE `Items`
MODIFY `item_id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `Users`
--
ALTER TABLE `Users`
MODIFY `user_id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=30;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `BasketItems`
--
ALTER TABLE `BasketItems`
ADD CONSTRAINT `item` FOREIGN KEY (`item_id`) REFERENCES `Items` (`item_id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `user` FOREIGN KEY (`user_id`) REFERENCES `Users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Items`
--
ALTER TABLE `Items`
ADD CONSTRAINT `item_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON UPDATE CASCADE;