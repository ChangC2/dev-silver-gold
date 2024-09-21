-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 20, 2024 at 09:03 AM
-- Server version: 10.4.6-MariaDB
-- PHP Version: 7.2.22

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ringfree_voicemail`
--

-- --------------------------------------------------------

--
-- Table structure for table `accounts`
--

CREATE TABLE `accounts` (
  `id` int(11) NOT NULL,
  `accountname` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `mobile_number` varchar(20) NOT NULL DEFAULT '',
  `status` tinyint(4) NOT NULL DEFAULT 0,
  `credits` float DEFAULT 0,
  `farm_name` varchar(255) NOT NULL DEFAULT '',
  `ip_address` varchar(25) NOT NULL DEFAULT '',
  `test_count` int(11) NOT NULL DEFAULT 0,
  `caller_id` varchar(20) NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `verification_code` varchar(255) NOT NULL DEFAULT '',
  `first_name` varchar(255) NOT NULL DEFAULT '',
  `last_name` varchar(255) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `accounts`
--

INSERT INTO `accounts` (`id`, `accountname`, `password`, `email`, `mobile_number`, `status`, `credits`, `farm_name`, `ip_address`, `test_count`, `caller_id`, `created_at`, `verification_code`, `first_name`, `last_name`) VALUES
(1, 'Xavier De la Piedra', '$2y$10$qJEa.ECM.JEjDue5Qq8BY.UZpIRzJdNdRqmc7P/lGp5UV/oJzRSLq', 'admin@admin.com', '6266742545', 1, 0, '', '', 0, '', '2024-01-30 02:10:07', '', 'Xavier', 'De la Piedra'),
(2, 'Xavier', '$2y$10$HvcbiRovmJhjHE4UWdaR8uP1Q5D3Yvg3Q4ibzOzYkzH6j7w3HRPVu', 'xavier@newhomepage.com', '6266742545', 1, 1104.4, 'Fidelity Total Farm', '183.182.110.34', 5, '5623152447', '2024-01-30 02:10:07', '', '', ''),
(14, 'Zhou Ming', '$2y$10$gj2SgDq0QdygtzTbok1YXe4Gy9UvrozQL3rt0YCbGYylFmexLeKn6', 'mzhou9954@gmail.com', '6266742545', 1, 1846.1, 'Test Farm', '192.168.0.53', 47, '', '2024-01-30 02:10:07', '', 'Zhou', 'Ming'),
(70, 'Bounthum Vanou', '$2y$10$JyymiiDaPSFCcHXUr0cKdOKv/KNxDDVklAQEhdXkBxpt5boyrxRGm', 'vientiane2021it@gmail.com', '1234567891', 1, 0, '', '', 0, '', '2024-05-07 12:30:14', '', 'Bounthum', 'Vanou');

-- --------------------------------------------------------

--
-- Table structure for table `account_group`
--

CREATE TABLE `account_group` (
  `id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `account_group`
--

INSERT INTO `account_group` (`id`, `account_id`, `group_id`) VALUES
(1, 1, 1),
(24, 13, 3),
(25, 14, 3),
(80, 68, 3),
(81, 69, 3),
(82, 70, 3);

-- --------------------------------------------------------

--
-- Table structure for table `clone_voices`
--

CREATE TABLE `clone_voices` (
  `id` int(10) UNSIGNED NOT NULL,
  `url` varchar(255) CHARACTER SET utf8 NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `user_id` int(11) NOT NULL,
  `ai_processing` int(11) NOT NULL DEFAULT 0,
  `voice_id` int(11) NOT NULL,
  `content` text CHARACTER SET utf8 NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `clone_voices`
--

INSERT INTO `clone_voices` (`id`, `url`, `created_at`, `user_id`, `ai_processing`, `voice_id`, `content`) VALUES
(636, '', '2024-05-25 04:40:26', 14, 0, 636, 'This is an example of your cloned voice being used from your recording'),
(637, '', '2024-05-27 16:44:19', 14, 0, 637, 'This is an example of your cloned voice being used from your recording');

-- --------------------------------------------------------

--
-- Table structure for table `contacts`
--

CREATE TABLE `contacts` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(200) CHARACTER SET utf8 NOT NULL DEFAULT 'New user',
  `phone` varchar(200) CHARACTER SET utf8 NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `user` int(11) NOT NULL,
  `group` int(11) NOT NULL,
  `email` varchar(200) CHARACTER SET utf8 NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `contacts`
--

INSERT INTO `contacts` (`id`, `name`, `phone`, `created_at`, `user`, `group`, `email`) VALUES
(567, 'Xavier3', '6266742545', '2023-11-24 13:11:08', 14, 963, ''),
(566, 'Xavier2', '6266742545', '2023-11-24 13:11:08', 14, 963, ''),
(958, 'JASMIN NAVARRO', '5204998471', '2023-11-29 12:49:07', 14, 962, 'xiao.com'),
(957, 'RANDALL LUTCAVICH', '3102930784', '2023-11-29 12:49:07', 14, 963, 'test@test.com'),
(534, 'Bou', '2500210413', '2023-11-02 19:57:34', 14, 965, ''),
(533, 'Ivan', '1266742545', '2023-11-02 19:57:34', 14, 962, ''),
(532, 'Xavier', '6266742545', '2023-11-02 19:57:34', 14, 963, 'Xavier.com'),
(959, 'Xavier', '6266742545', '2023-12-28 09:38:49', 14, 965, 'xiaomeijing2008@gmail.com'),
(956, 'JORGE MANSILLA', '5622179933', '2023-11-29 12:49:07', 14, 962, ''),
(955, 'TERESA GUTIERREZ', '5628439006', '2023-11-29 12:49:07', 14, 962, ''),
(901, 'RANDALL LUTCAVICH', '3102930784', '2023-11-25 00:18:50', 14, 962, ''),
(899, 'TERESA GUTIERREZ', '5628439006', '2023-11-25 00:18:50', 14, 962, ''),
(900, 'JORGE MANSILLA', '5622179933', '2023-11-25 00:18:50', 14, 962, ''),
(969, 'Test', '1231312313', '2024-03-04 11:04:19', 14, 0, '');

-- --------------------------------------------------------

--
-- Table structure for table `contact_group`
--

CREATE TABLE `contact_group` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(200) CHARACTER SET utf8 NOT NULL DEFAULT 'New user',
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `user` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `contact_group`
--

INSERT INTO `contact_group` (`id`, `name`, `created_at`, `user`) VALUES
(962, 'Company', '2024-01-11 12:46:44', 14),
(963, 'Family', '2024-01-11 12:49:09', 14),
(965, 'Friends', '2024-01-11 12:49:09', 14);

-- --------------------------------------------------------

--
-- Table structure for table `cowboy_log`
--

CREATE TABLE `cowboy_log` (
  `id` int(10) UNSIGNED NOT NULL,
  `content` text CHARACTER SET utf8 NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `log_id` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `cowboy_log`
--

INSERT INTO `cowboy_log` (`id`, `content`, `created_at`, `log_id`) VALUES
(548, 'Contact: +16266742545 Caller ID: 5623152337', '2024-02-27 09:51:41', 7451),
(549, 'Contact: +16266742545 Caller ID: 5623152329', '2024-02-27 09:51:45', 7452),
(550, 'Contact: +16266742545 Caller ID: 5623152290', '2024-05-24 02:11:09', 7666),
(551, 'Contact: +15204998471 Caller ID: 5623152290', '2024-05-24 02:11:09', 7667),
(552, 'Contact: +13102930784 Caller ID: 5623152290', '2024-05-24 02:11:09', 7668),
(553, 'Contact: +15622179933 Caller ID: 5623152290', '2024-05-24 02:11:09', 7669),
(554, 'Contact: +15628439006 Caller ID: 5623152290', '2024-05-24 02:11:09', 7670),
(555, 'Contact: +13102930784 Caller ID: 5623152290', '2024-05-24 02:11:09', 7671),
(556, 'Contact: +15622179933 Caller ID: 5623152290', '2024-05-24 02:11:09', 7672),
(557, 'Contact: +16266742545 Caller ID: 5623152270', '2024-05-24 02:12:03', 7653),
(558, 'Contact: +15204998471 Caller ID: 5623152270', '2024-05-24 02:12:03', 7654),
(559, 'Contact: +13102930784 Caller ID: 5623152270', '2024-05-24 02:12:03', 7655),
(560, 'Contact: +15622179933 Caller ID: 5623152270', '2024-05-24 02:12:03', 7656),
(561, 'Contact: +15628439006 Caller ID: 5623152270', '2024-05-24 02:12:03', 7657),
(562, 'Contact: +13102930784 Caller ID: 5623152270', '2024-05-24 02:12:03', 7658),
(563, 'Contact: +15622179933 Caller ID: 5623152270', '2024-05-24 02:12:03', 7659),
(564, 'Contact: +15628439006 Caller ID: 5623152270', '2024-05-24 02:12:03', 7660),
(565, 'Contact: +16266742545 Caller ID: 5623152270', '2024-05-24 02:12:03', 7661),
(566, 'Contact: +16266742545 Caller ID: 5623152270', '2024-05-24 02:12:03', 7662),
(567, 'Contact: +12500210413 Caller ID: 5623152252', '2024-05-24 02:13:47', 7663),
(568, 'Contact: +11266742545 Caller ID: 5623152252', '2024-05-24 02:13:47', 7664),
(569, 'Contact: +16266742545 Caller ID: 5623152252', '2024-05-24 02:13:47', 7665),
(570, 'Contact: +16266742545 Caller ID: 5623152251', '2024-05-24 02:14:25', 7640),
(571, 'Contact: +15204998471 Caller ID: 5623152251', '2024-05-24 02:14:25', 7641),
(572, 'Contact: +13102930784 Caller ID: 5623152251', '2024-05-24 02:14:25', 7642),
(573, 'Contact: +15622179933 Caller ID: 5623152251', '2024-05-24 02:14:25', 7643),
(574, 'Contact: +15628439006 Caller ID: 5623152251', '2024-05-24 02:14:25', 7644),
(575, 'Contact: +13102930784 Caller ID: 5623152251', '2024-05-24 02:14:25', 7645),
(576, 'Contact: +15622179933 Caller ID: 5623152251', '2024-05-24 02:14:25', 7646),
(577, 'Contact: +15628439006 Caller ID: 5623152251', '2024-05-24 02:14:25', 7647),
(578, 'Contact: +16266742545 Caller ID: 5623152251', '2024-05-24 02:14:25', 7648),
(579, 'Contact: +16266742545 Caller ID: 5623152251', '2024-05-24 02:14:25', 7649),
(580, 'Contact: +12500210413 Caller ID: 5623152234', '2024-05-24 02:15:09', 7650),
(581, 'Contact: +11266742545 Caller ID: 5623152234', '2024-05-24 02:15:09', 7651),
(582, 'Contact: +16266742545 Caller ID: 5623152234', '2024-05-24 02:15:09', 7652),
(583, 'Contact: +16266742545 Caller ID: 5623152221', '2024-05-24 02:17:04', 7679),
(584, 'Contact: +15204998471 Caller ID: 5623152221', '2024-05-24 02:17:04', 7680),
(585, 'Contact: +13102930784 Caller ID: 5623152221', '2024-05-24 02:17:04', 7681);

-- --------------------------------------------------------

--
-- Table structure for table `groups`
--

CREATE TABLE `groups` (
  `id` int(11) NOT NULL,
  `group_name` varchar(255) NOT NULL,
  `permission` int(11) NOT NULL DEFAULT 1,
  `created_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `groups`
--

INSERT INTO `groups` (`id`, `group_name`, `permission`, `created_at`) VALUES
(1, 'Super Administrator', 0, '2024-04-06 02:03:06'),
(2, 'Dev', 2, '2024-04-06 02:03:06'),
(3, 'User', 1, '2024-04-06 02:03:06');

-- --------------------------------------------------------

--
-- Table structure for table `ips`
--

CREATE TABLE `ips` (
  `id` int(10) UNSIGNED NOT NULL,
  `ip_address` varchar(255) CHARACTER SET utf8 NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `ips`
--

INSERT INTO `ips` (`id`, `ip_address`) VALUES
(606, '183.182.115.56'),
(605, '183.182.114.206'),
(607, '183.182.114.80'),
(608, '74.205.86.152'),
(609, '189.173.59.190'),
(610, '74.205.86.153'),
(611, '183.182.110.34'),
(612, '189.173.140.130'),
(613, '183.182.114.141');

-- --------------------------------------------------------

--
-- Table structure for table `mail_log`
--

CREATE TABLE `mail_log` (
  `id` int(10) UNSIGNED NOT NULL,
  `url` varchar(255) CHARACTER SET utf8 NOT NULL,
  `record_name` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `user_id` int(11) NOT NULL,
  `user_name` varchar(200) CHARACTER SET utf8 NOT NULL DEFAULT 'New user',
  `receiver` varchar(200) CHARACTER SET utf8 NOT NULL DEFAULT 'New user',
  `status` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `order_id` int(11) NOT NULL DEFAULT 0,
  `receiver_name` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `reason` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `type` tinyint(4) NOT NULL DEFAULT 0,
  `voice_id` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `content` text CHARACTER SET utf8 NOT NULL,
  `ai_processing` tinyint(4) NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `mail_log`
--

INSERT INTO `mail_log` (`id`, `url`, `record_name`, `created_at`, `user_id`, `user_name`, `receiver`, `status`, `order_id`, `receiver_name`, `reason`, `type`, `voice_id`, `content`, `ai_processing`) VALUES
(7640, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:37', 14, 'Zhou Ming', '6266742545', 'Processing', 797, 'Xavier', '', 0, '', '', 0),
(7641, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:37', 14, 'Zhou Ming', '5204998471', 'Processing', 797, 'JASMIN NAVARRO', '', 0, '', '', 0),
(7642, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:37', 14, 'Zhou Ming', '3102930784', 'Processing', 797, 'RANDALL LUTCAVICH', '', 0, '', '', 0),
(7643, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:37', 14, 'Zhou Ming', '5622179933', 'Processing', 797, 'JORGE MANSILLA', '', 0, '', '', 0),
(7644, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:37', 14, 'Zhou Ming', '5628439006', 'Processing', 797, 'TERESA GUTIERREZ', '', 0, '', '', 0),
(7645, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:37', 14, 'Zhou Ming', '3102930784', 'Processing', 797, 'RANDALL LUTCAVICH', '', 0, '', '', 0),
(7646, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:37', 14, 'Zhou Ming', '5622179933', 'Processing', 797, 'JORGE MANSILLA', '', 0, '', '', 0),
(7647, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:37', 14, 'Zhou Ming', '5628439006', 'Processing', 797, 'TERESA GUTIERREZ', '', 0, '', '', 0),
(7648, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:37', 14, 'Zhou Ming', '6266742545', 'Processing', 797, 'Xavier3', '', 0, '', '', 0),
(7649, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:37', 14, 'Zhou Ming', '6266742545', 'Processing', 797, 'Xavier2', '', 0, '', '', 0),
(7650, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:37', 14, 'Zhou Ming', '2500210413', 'Processing', 797, 'Bou', '', 0, '', '', 0),
(7651, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:37', 14, 'Zhou Ming', '1266742545', 'Processing', 797, 'Ivan', '', 0, '', '', 0),
(7652, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:37', 14, 'Zhou Ming', '6266742545', 'Processing', 797, 'Xavier', '', 0, '', '', 0),
(7653, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:53', 14, 'Zhou Ming', '6266742545', 'Processing', 798, 'Xavier', '', 0, '', '', 0),
(7654, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:53', 14, 'Zhou Ming', '5204998471', 'Processing', 798, 'JASMIN NAVARRO', '', 0, '', '', 0),
(7655, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:53', 14, 'Zhou Ming', '3102930784', 'Processing', 798, 'RANDALL LUTCAVICH', '', 0, '', '', 0),
(7656, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:53', 14, 'Zhou Ming', '5622179933', 'Processing', 798, 'JORGE MANSILLA', '', 0, '', '', 0),
(7657, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:53', 14, 'Zhou Ming', '5628439006', 'Processing', 798, 'TERESA GUTIERREZ', '', 0, '', '', 0),
(7658, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:53', 14, 'Zhou Ming', '3102930784', 'Processing', 798, 'RANDALL LUTCAVICH', '', 0, '', '', 0),
(7659, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:53', 14, 'Zhou Ming', '5622179933', 'Processing', 798, 'JORGE MANSILLA', '', 0, '', '', 0),
(7660, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:53', 14, 'Zhou Ming', '5628439006', 'Processing', 798, 'TERESA GUTIERREZ', '', 0, '', '', 0),
(7661, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:53', 14, 'Zhou Ming', '6266742545', 'Processing', 798, 'Xavier3', '', 0, '', '', 0),
(7662, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:53', 14, 'Zhou Ming', '6266742545', 'Processing', 798, 'Xavier2', '', 0, '', '', 0),
(7663, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:53', 14, 'Zhou Ming', '2500210413', 'Processing', 798, 'Bou', '', 0, '', '', 0),
(7664, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:53', 14, 'Zhou Ming', '1266742545', 'Processing', 798, 'Ivan', '', 0, '', '', 0),
(7665, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:56:53', 14, 'Zhou Ming', '6266742545', 'Processing', 798, 'Xavier', '', 0, '', '', 0),
(7666, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:57:03', 14, 'Zhou Ming', '6266742545', 'Processing', 799, 'Xavier', '', 0, '', '', 0),
(7667, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:57:03', 14, 'Zhou Ming', '5204998471', 'Processing', 799, 'JASMIN NAVARRO', '', 0, '', '', 0),
(7668, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:57:03', 14, 'Zhou Ming', '3102930784', 'Processing', 799, 'RANDALL LUTCAVICH', '', 0, '', '', 0),
(7669, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:57:03', 14, 'Zhou Ming', '5622179933', 'Processing', 799, 'JORGE MANSILLA', '', 0, '', '', 0),
(7670, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:57:03', 14, 'Zhou Ming', '5628439006', 'Processing', 799, 'TERESA GUTIERREZ', '', 0, '', '', 0),
(7671, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:57:03', 14, 'Zhou Ming', '3102930784', 'Processing', 799, 'RANDALL LUTCAVICH', '', 0, '', '', 0),
(7672, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:57:03', 14, 'Zhou Ming', '5622179933', 'Processing', 799, 'JORGE MANSILLA', '', 0, '', '', 0),
(7673, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:57:03', 14, 'Zhou Ming', '5628439006', 'Pending in Queue', 800, 'TERESA GUTIERREZ', '', 0, '', '', 0),
(7674, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:57:03', 14, 'Zhou Ming', '6266742545', 'Pending in Queue', 800, 'Xavier3', '', 0, '', '', 0),
(7675, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:57:03', 14, 'Zhou Ming', '6266742545', 'Pending in Queue', 800, 'Xavier2', '', 0, '', '', 0),
(7676, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:57:03', 14, 'Zhou Ming', '2500210413', 'Pending in Queue', 800, 'Bou', '', 0, '', '', 0),
(7677, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:57:03', 14, 'Zhou Ming', '1266742545', 'Pending in Queue', 800, 'Ivan', '', 0, '', '', 0),
(7678, 'http://localhost/retail_voicemail/audios/20240509002239.mp3', 'Sample Recording 3', '2024-05-23 11:57:03', 14, 'Zhou Ming', '6266742545', 'Pending in Queue', 800, 'Xavier', '', 0, '', '', 0),
(7686, '', 'Female Voice', '2024-05-23 11:58:37', 14, 'Zhou Ming', '5628439006', 'Pending in Queue', 802, 'TERESA GUTIERREZ', '', 1, '581', 'Hope all is well TERESA GUTIERREZ.\nHello John.  This is a test of the Ring Less Voicemail system using a AI Avatar voice.   John I understand you may be in need of a new larger home for your growing family.   Please give me a call and we can get you on your way to your dream home.', 0),
(7687, '', 'Female Voice', '2024-05-23 11:58:37', 14, 'Zhou Ming', '6266742545', 'Pending in Queue', 802, 'Xavier3', '', 1, '581', 'Hope all is well Xavier3.\nHello John.  This is a test of the Ring Less Voicemail system using a AI Avatar voice.   John I understand you may be in need of a new larger home for your growing family.   Please give me a call and we can get you on your way to your dream home.', 0),
(7688, '', 'Female Voice', '2024-05-23 11:58:37', 14, 'Zhou Ming', '6266742545', 'Pending in Queue', 802, 'Xavier2', '', 1, '581', 'Hope all is well Xavier2.\nHello John.  This is a test of the Ring Less Voicemail system using a AI Avatar voice.   John I understand you may be in need of a new larger home for your growing family.   Please give me a call and we can get you on your way to your dream home.', 0),
(7689, '', 'Female Voice', '2024-05-23 11:58:37', 14, 'Zhou Ming', '2500210413', 'Pending in Queue', 802, 'Bou', '', 1, '581', 'Hope all is well Bou.\nHello John.  This is a test of the Ring Less Voicemail system using a AI Avatar voice.   John I understand you may be in need of a new larger home for your growing family.   Please give me a call and we can get you on your way to your dream home.', 0),
(7690, '', 'Female Voice', '2024-05-23 11:58:37', 14, 'Zhou Ming', '1266742545', 'Pending in Queue', 802, 'Ivan', '', 1, '581', 'Hope all is well Ivan.\nHello John.  This is a test of the Ring Less Voicemail system using a AI Avatar voice.   John I understand you may be in need of a new larger home for your growing family.   Please give me a call and we can get you on your way to your dream home.', 0),
(7691, '', 'Female Voice', '2024-05-23 11:58:37', 14, 'Zhou Ming', '6266742545', 'Pending in Queue', 802, 'Xavier', '', 1, '581', 'Hope all is well Xavier.\nHello John.  This is a test of the Ring Less Voicemail system using a AI Avatar voice.   John I understand you may be in need of a new larger home for your growing family.   Please give me a call and we can get you on your way to your dream home.', 0);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `id` int(10) UNSIGNED NOT NULL,
  `forwarding_number` varchar(50) CHARACTER SET utf8 NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `user_id` int(11) NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT 'New user',
  `url` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `order_no` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `record_name` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '0: Pending, 1: Delivered',
  `user_email` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `farm_name` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `record_count` int(11) NOT NULL DEFAULT 0,
  `note` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `type` tinyint(4) NOT NULL DEFAULT 0,
  `content` text CHARACTER SET utf8 NOT NULL,
  `voice_id` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `send_time` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT ''
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `forwarding_number`, `created_at`, `user_id`, `user_name`, `url`, `order_no`, `record_name`, `status`, `user_email`, `farm_name`, `record_count`, `note`, `type`, `content`, `voice_id`, `send_time`) VALUES
(797, '6266742545', '2024-05-23 11:56:37', 14, 'Zhou Ming', 'http://localhost/retail_voicemail/audios/20240509002239.mp3', '887993', 'Sample Recording 3', 0, 'mzhou9954@gmail.com', '', 13, '', 0, '', '', ''),
(798, '6266742545', '2024-05-23 11:56:53', 14, 'Zhou Ming', 'http://localhost/retail_voicemail/audios/20240509002239.mp3', '944425', 'Sample Recording 3', 0, 'mzhou9954@gmail.com', '', 13, '', 0, '', '', '2024-05-23 08:00:00'),
(799, '6266742545', '2024-05-23 11:57:03', 14, 'Zhou Ming', 'http://localhost/retail_voicemail/audios/20240509002239.mp3', '799520 (Scheduled-1)', 'Sample Recording 3', 0, 'mzhou9954@gmail.com', '', 7, '', 0, '', '', '2024-05-23 08:00:00'),
(800, '6266742545', '2024-05-23 11:57:03', 14, 'Zhou Ming', 'http://localhost/retail_voicemail/audios/20240509002239.mp3', '799520 (Scheduled-2)', 'Sample Recording 3', 0, 'mzhou9954@gmail.com', '', 6, '', 0, '', '', '2024-05-24 08:00:00'),
(802, '6266742545', '2024-05-23 11:58:37', 14, 'Zhou Ming', 'https://videodrop.leadmarketer.com/default.mp3', '956001 (Scheduled-2)', 'Female Voice', 0, 'mzhou9954@gmail.com', '', 6, '', 1, 'Hope all is well First Name.\nHello John.  This is a test of the Ring Less Voicemail system using a AI Avatar voice.   John I understand you may be in need of a new larger home for your growing family.   Please give me a call and we can get you on your way to your dream home.', '581', '');

-- --------------------------------------------------------

--
-- Table structure for table `purchase_history`
--

CREATE TABLE `purchase_history` (
  `id` int(10) UNSIGNED NOT NULL,
  `credits_amount` varchar(10) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `user_id` int(11) NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT 'New user',
  `paid_amount` varchar(10) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `user_email` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `purchase_type` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `description` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `card_name` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `address_cc` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `city` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `state` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `street` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `zip` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT ''
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `purchase_history`
--

INSERT INTO `purchase_history` (`id`, `credits_amount`, `created_at`, `user_id`, `user_name`, `paid_amount`, `user_email`, `purchase_type`, `description`, `card_name`, `address_cc`, `city`, `state`, `street`, `zip`) VALUES
(691, '1250', '2024-01-05 16:02:40', 14, 'Zhou', '125.00', 'mzhou9954@gmail.com', 'Stripe', 'Paid on Website', '', '', '', '', '', ''),
(692, '120', '2024-01-06 20:49:23', 14, 'Zhou', '24.00', 'mzhou9954@gmail.com', 'Stripe', 'Paid on Website', '', '', '', '', '', ''),
(693, '120', '2024-01-06 21:49:21', 14, 'Zhou Ming', '24.00', 'mzhou9954@gmail.com', 'Stripe', 'Paid on Website', '', '', '', '', '', ''),
(694, '120', '2024-01-06 23:37:43', 14, 'Zhou Ming', '240.00', 'mzhou9954@gmail.com', 'Stripe', 'Paid on Website', '', '', '', '', '', ''),
(695, '150', '2024-01-11 15:57:47', 14, 'Zhou Ming', '300.00', 'mzhou9954@gmail.com', 'Stripe', 'Paid on Website', '', '', '', '', '', ''),
(696, '200', '2024-01-11 16:02:26', 14, 'Zhou Ming', '400.00', 'mzhou9954@gmail.com', 'Stripe', 'Paid on Website', '', '', '', '', '', ''),
(697, '125', '2024-01-11 21:48:51', 14, 'Zhou Ming', '250.00', 'mzhou9954@gmail.com', 'Stripe', 'Paid on Website', '', '', '', '', '', ''),
(698, '120', '2024-03-04 01:22:15', 14, 'Zhou Ming', '240.00', 'mzhou9954@gmail.com', 'Stripe', 'Paid on Website', '', '', '', '', '', ''),
(699, '35', '2024-03-26 22:05:24', 14, 'Zhou Ming', '70.00', 'mzhou9954@gmail.com', 'Stripe', 'Paid on Website', '', '', '', '', '', ''),
(700, '100', '2024-04-19 21:57:42', 14, 'Zhou Ming', '200.00', 'mzhou9954@gmail.com', 'Stripe', 'Paid on Website', 'Test', 'test', 'test', 'test', 'test', '42424'),
(701, '100', '2024-04-19 23:09:31', 14, 'Zhou Ming', '200.00', 'mzhou9954@gmail.com', 'Stripe', 'Paid on Website', 'Test', 'Richmond', 'Los Angels', 'California', 'Los Angels', '42424'),
(702, '120', '2024-04-19 23:13:14', 14, 'Zhou Ming', '240.00', 'mzhou9954@gmail.com', 'Stripe', 'Paid on Website', 'sdfsdf', 'sdfsdf', 'sdfsdf', 'sdffsd', 'sdfsdf', '42424'),
(703, '111', '2024-04-19 23:18:29', 14, 'Zhou Ming', '222.00', 'mzhou9954@gmail.com', 'Stripe', 'Paid on Website', 'asdsad', 'asad', 'asdasd', 'asdasd', 'asdasd', '42424'),
(704, '100', '2024-06-05 14:14:47', 14, 'Zhou Ming', '200.00', 'mzhou9954@gmail.com', 'Stripe', 'Paid on Website', 'Test', 'Test', 'Test', 'Tee', 'Test', 'Test'),
(705, '200', '2024-06-05 14:14:47', 14, 'Zhou Ming', '400.00', 'mzhou9954@gmail.com', 'Stripe', 'Paid on Website', 'T', 't', 't', 't', 't', 't'),
(706, '100', '2024-06-05 14:17:31', 14, 'Zhou Ming', '200.00', 'mzhou9954@gmail.com', 'Stripe', 'Paid on Website', 'C', 'C', 'c', 'c', 'c', 'c'),
(707, '200', '2024-06-05 14:18:31', 14, 'Zhou Ming', '400.00', 'mzhou9954@gmail.com', 'Stripe', 'Paid on Website', 'b', 'b', 'b', 'b', 'b', 'b'),
(708, '122', '2024-06-05 14:22:15', 14, 'Zhou Ming', '244.00', 'mzhou9954@gmail.com', 'Stripe', 'Paid on Website', 'a', 'a', 'a', 'a', 'a', 'a');

-- --------------------------------------------------------

--
-- Table structure for table `recordings`
--

CREATE TABLE `recordings` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(200) CHARACTER SET utf8 NOT NULL DEFAULT 'New user',
  `url` varchar(200) CHARACTER SET utf8 NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `user_id` int(11) NOT NULL,
  `user_name` varchar(40) CHARACTER SET utf8 NOT NULL DEFAULT 'New user'
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `recordings`
--

INSERT INTO `recordings` (`id`, `name`, `url`, `created_at`, `user_id`, `user_name`) VALUES
(559, 'Sample Recording 1', 'http://localhost/retail_voicemail/audios/20240508034259.mp3', '2024-05-08 03:59:34', 14, 'Zhou Ming'),
(555, 'Xavier', 'https://videodrop.leadmarketer.com/default.mp3', '2024-02-29 03:22:36', 14, 'Zhou Ming'),
(560, 'Sample Recording 2', 'http://localhost/retail_voicemail/audios/20240508132457.mp3', '2024-05-08 03:59:34', 14, 'Zhou Ming'),
(561, 'Sample Recording 3', 'http://localhost/retail_voicemail/audios/20240509002239.mp3', '2024-05-09 00:38:54', 14, 'Zhou Ming');

-- --------------------------------------------------------

--
-- Table structure for table `scripts`
--

CREATE TABLE `scripts` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(200) CHARACTER SET utf8 NOT NULL DEFAULT 'New user',
  `content` text CHARACTER SET utf8 NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `user_id` int(11) NOT NULL,
  `user_name` varchar(40) CHARACTER SET utf8 NOT NULL DEFAULT 'New user',
  `salutation` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT ''
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `scripts`
--

INSERT INTO `scripts` (`id`, `name`, `content`, `created_at`, `user_id`, `user_name`, `salutation`) VALUES
(538, 'John 1', 'Hello John.  This is a test of the Ring Less Voicemail system using a AI Avatar voice.   John I understand you may be in need of a new larger home for your growing family.   Please give me a call and we can get you on your way to your dream home.', '2024-01-25 14:27:28', 14, 'Zhou Ming', ''),
(537, 'John 2', 'This is John, your local realtor and long time resident of Long Beach. I wanted to reach out and inform you about an upcoming free electronic disposal event this weekend. As a part of our community initiative, I\'d like to invite you to participate. It\'s a great opportunity to responsibly dispose of electronic waste. Feel free to drop by, and I hope to see you there! Any questions give me a call', '2024-01-25 13:59:24', 14, 'Zhou Ming', 'Hope all is well First Name'),
(549, 'Going out', 'going out and ', '2024-03-31 22:38:18', 14, 'Zhou Ming', 'First Name');

-- --------------------------------------------------------

--
-- Table structure for table `signs`
--

CREATE TABLE `signs` (
  `id` int(10) UNSIGNED NOT NULL,
  `sign` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `user_id` int(11) NOT NULL,
  `user_name` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'New user'
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `signs`
--

INSERT INTO `signs` (`id`, `sign`, `created_at`, `user_id`, `user_name`) VALUES
(528, 'data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/4gHYSUNDX1BST0ZJTEUAAQEAAAHIAAAAAAQwAABtbnRyUkdCIFhZWiAH4AABAAEAAAAAAABhY3NwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAA9tYAAQAAAADTLQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAlkZXNjAAAA8AAAACRyWFlaAAABFAAAABRnWFlaAAABKAAAABRiWFlaAAABPAAAABR3dHB0AAABUAAAABRyVFJDAAABZAAAAChnVFJDAAABZAAAAChiVFJDAAABZAAAAChjcHJ0AAABjAAAADxtbHVjAAAAAAAAAAEAAAAMZW5VUwAAAAgAAAAcAHMAUgBHAEJYWVogAAAAAAAAb6IAADj1AAADkFhZWiAAAAAAAABimQAAt4UAABjaWFlaIAAAAAAAACSgAAAPhAAAts9YWVogAAAAAAAA9tYAAQAAAADTLXBhcmEAAAAAAAQAAAACZmYAAPKnAAANWQAAE9AAAApbAAAAAAAAAABtbHVjAAAAAAAAAAEAAAAMZW5VUwAAACAAAAAcAEcAbwBvAGcAbABlACAASQBuAGMALgAgADIAMAAxADb/2wBDAAMCAgICAgMCAgIDAwMDBAYEBAQEBAgGBgUGCQgKCgkICQkKDA8MCgsOCwkJDRENDg8QEBEQCgwSExIQEw8QEBD/2wBDAQMDAwQDBAgEBAgQCwkLEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBD/wAARCADIAZADASIAAhEBAxEB/8QAFQABAQAAAAAAAAAAAAAAAAAAAAn/xAAUEAEAAAAAAAAAAAAAAAAAAAAA/8QAFAEBAAAAAAAAAAAAAAAAAAAAAP/EABQRAQAAAAAAAAAAAAAAAAAAAAD/2gAMAwEAAhEDEQA/AJVAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//Z', '2024-04-09 14:35:07', 14, 'Zhou Ming'),
(529, 'data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/4gHYSUNDX1BST0ZJTEUAAQEAAAHIAAAAAAQwAABtbnRyUkdCIFhZWiAH4AABAAEAAAAAAABhY3NwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAA9tYAAQAAAADTLQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAlkZXNjAAAA8AAAACRyWFlaAAABFAAAABRnWFlaAAABKAAAABRiWFlaAAABPAAAABR3dHB0AAABUAAAABRyVFJDAAABZAAAAChnVFJDAAABZAAAAChiVFJDAAABZAAAAChjcHJ0AAABjAAAADxtbHVjAAAAAAAAAAEAAAAMZW5VUwAAAAgAAAAcAHMAUgBHAEJYWVogAAAAAAAAb6IAADj1AAADkFhZWiAAAAAAAABimQAAt4UAABjaWFlaIAAAAAAAACSgAAAPhAAAts9YWVogAAAAAAAA9tYAAQAAAADTLXBhcmEAAAAAAAQAAAACZmYAAPKnAAANWQAAE9AAAApbAAAAAAAAAABtbHVjAAAAAAAAAAEAAAAMZW5VUwAAACAAAAAcAEcAbwBvAGcAbABlACAASQBuAGMALgAgADIAMAAxADb/2wBDAAMCAgICAgMCAgIDAwMDBAYEBAQEBAgGBgUGCQgKCgkICQkKDA8MCgsOCwkJDRENDg8QEBEQCgwSExIQEw8QEBD/2wBDAQMDAwQDBAgEBAgQCwkLEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBD/wAARCADIAZADASIAAhEBAxEB/8QAFQABAQAAAAAAAAAAAAAAAAAAAAn/xAAUEAEAAAAAAAAAAAAAAAAAAAAA/8QAFAEBAAAAAAAAAAAAAAAAAAAAAP/EABQRAQAAAAAAAAAAAAAAAAAAAAD/2gAMAwEAAhEDEQA/AJVAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//Z', '2024-05-06 22:32:45', 70, 'Boun Thum');

-- --------------------------------------------------------

--
-- Table structure for table `usage_history`
--

CREATE TABLE `usage_history` (
  `id` int(10) UNSIGNED NOT NULL,
  `credits_amount` varchar(10) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `user_id` int(11) NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT 'New user',
  `user_email` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `description` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT ''
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `usage_history`
--

INSERT INTO `usage_history` (`id`, `credits_amount`, `created_at`, `user_id`, `user_name`, `user_email`, `description`) VALUES
(682, '-0.2', '2024-01-03 09:50:52', 14, 'Zhou', 'mzhou9954@gmail.com', 'Order ID:887855  Records:2'),
(683, '-0.2', '2024-01-03 09:58:18', 14, 'Zhou', 'mzhou9954@gmail.com', 'Order ID:672361  Records:2'),
(684, '-0.2', '2024-01-06 21:49:59', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:476661  Records:2'),
(685, '-0.4', '2024-01-06 21:55:14', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:918026  Records:2'),
(686, '-0.3', '2024-01-06 23:38:30', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:247438  Records:3'),
(687, '-1.3', '2024-03-26 22:19:31', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:805621  Ringless Voicemail:Test Farm  Recordings:13'),
(688, '0.1', '2024-03-27 02:18:35', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:666784  Undelivered Credit Recordings:1'),
(689, '0.1', '2024-03-27 02:19:18', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:282708  Undelivered Credit Recordings:1'),
(690, '0', '2024-03-27 02:57:10', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:788466  Recordings:0'),
(691, '0', '2024-03-27 02:57:46', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:629500  Recordings:0'),
(692, '0', '2024-03-27 04:14:00', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:873424  Recordings:0'),
(693, '0', '2024-03-27 04:14:34', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:619329  Recordings:0'),
(694, '0', '2024-03-27 04:16:42', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:281275  Recordings:0'),
(695, '0', '2024-03-27 04:17:30', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:265181  Recordings:0'),
(696, '-1.3', '2024-03-27 04:19:30', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:330154  Recordings:13'),
(697, '0', '2024-03-27 04:19:36', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:265181  Undelivered Credit Recordings:0'),
(698, '0', '2024-03-27 04:19:39', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:281275  Undelivered Credit Recordings:0'),
(699, '0', '2024-03-27 04:19:42', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:619329  Undelivered Credit Recordings:0'),
(700, '-0.8', '2024-03-27 04:19:57', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:802201  Recordings:8'),
(701, '-0.5', '2024-03-27 04:29:59', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:415555  Recordings:1'),
(702, '0.5', '2024-03-27 04:31:12', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:415555  Undelivered Credit Recordings:1'),
(703, '0', '2024-03-27 04:31:39', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:788466  Undelivered Credit Recordings:0'),
(704, '0', '2024-03-27 04:31:42', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:629500  Undelivered Credit Recordings:0'),
(705, '0', '2024-03-27 04:31:45', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:873424  Undelivered Credit Recordings:0'),
(706, '-1.5', '2024-03-31 23:20:08', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:794133  Recordings:3'),
(707, '-4', '2024-04-01 00:43:34', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:261911  Recordings:8'),
(708, '-1.5', '2024-04-01 00:54:57', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:848191  Recordings:3'),
(709, '-4', '2024-04-01 00:59:18', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:268038  Recordings:8'),
(710, '-4', '2024-04-01 00:59:49', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:941752  Recordings:8'),
(711, '-1', '2024-04-01 01:01:23', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:388163  Recordings:2'),
(712, '4', '2024-04-01 01:01:36', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:941752  Undelivered Credit Recordings:8'),
(713, '-0.5', '2024-05-06 23:18:55', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 610084\nTotal Recordings : 1'),
(714, '0.5', '2024-05-06 23:24:44', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 224876\nUndelivered Recordings : 1'),
(715, '6.5', '2024-05-06 23:24:47', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 330154\nUndelivered Recordings : 13'),
(716, '4', '2024-05-06 23:24:51', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 802201\nUndelivered Recordings : 8'),
(717, '1.5', '2024-05-06 23:24:56', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 848191\nUndelivered Recordings : 3'),
(718, '0.5', '2024-05-06 23:24:58', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 610084\nUndelivered Recordings : 1'),
(719, '1.5', '2024-05-06 23:25:06', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 794133\nUndelivered Recordings : 3'),
(720, '4', '2024-05-06 23:25:13', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 261911\nUndelivered Recordings : 8'),
(721, '1', '2024-05-06 23:25:16', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 388163\nUndelivered Recordings : 2'),
(722, '4', '2024-05-06 23:25:19', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 268038\nUndelivered Recordings : 8'),
(723, '-0.5', '2024-05-06 23:26:22', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 240102\nTotal Recordings : 1'),
(724, '0', '2024-05-09 01:11:52', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 274111\nTotal Recordings : 0'),
(725, '0', '2024-05-09 01:37:04', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 274111\nUndelivered Recordings : 0'),
(726, '0', '2024-05-09 01:38:44', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 266998\nTotal Recordings : 0'),
(727, '0', '2024-05-09 01:40:10', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 266998\nUndelivered Recordings : 0'),
(728, '-6.5', '2024-05-09 01:42:41', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 100161\nTotal Recordings : 13'),
(729, '-1', '2024-05-09 02:02:12', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 786778\nTotal Recordings : 2'),
(730, '-2', '2024-05-09 02:04:39', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 952925\nTotal Recordings : 4'),
(731, '2', '2024-05-09 02:28:12', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 952925\nUndelivered Recordings : 4'),
(732, '0.5', '2024-05-09 02:28:15', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 240102\nUndelivered Recordings : 1'),
(733, '6.5', '2024-05-09 02:28:18', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 100161\nUndelivered Recordings : 13'),
(734, '-1', '2024-05-09 02:29:19', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 349711\nTotal Recordings : 2'),
(735, '-0.5', '2024-05-09 02:29:25', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 675846\nTotal Recordings : 1'),
(736, '-0.5', '2024-05-09 02:30:52', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 997063\nTotal Recordings : 1'),
(737, '-1', '2024-05-09 02:30:55', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 853710\nTotal Recordings : 2'),
(738, '-1', '2024-05-09 02:32:00', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 516497\nTotal Recordings : 2'),
(739, '-2', '2024-05-09 02:35:36', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 969351\nTotal Recordings : 4'),
(740, '0.2', '2024-05-15 01:20:58', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 349711\nUndelivered Recordings : 2'),
(741, '0.1', '2024-05-15 01:21:02', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 675846\nUndelivered Recordings : 1'),
(742, '0.1', '2024-05-15 01:21:05', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 997063\nUndelivered Recordings : 1'),
(743, '0.2', '2024-05-15 01:21:08', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 853710\nUndelivered Recordings : 2'),
(744, '0.4', '2024-05-15 01:21:10', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 969351\nUndelivered Recordings : 4'),
(745, '0.2', '2024-05-15 01:30:37', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 516497\nUndelivered Recordings : 2'),
(746, '-0.2', '2024-05-15 01:37:07', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 665709\nTotal Recordings : 2'),
(747, '-0.2', '2024-05-15 02:30:21', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 282818\nTotal Recordings : 2'),
(748, '0.2', '2024-05-15 03:19:22', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 282818\nUndelivered Recordings : 2'),
(749, '-1.3', '2024-05-23 02:45:19', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 955205\nTotal Recordings : 13'),
(750, '-0.2', '2024-05-23 04:03:16', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 757875\nTotal Recordings : 2'),
(751, '0.2', '2024-05-23 04:03:31', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 665709\nUndelivered Recordings : 2'),
(752, '-0.2', '2024-05-23 04:04:09', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 124818\nTotal Recordings : 2'),
(753, '-0.2', '2024-05-23 04:04:09', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 124818\nTotal Recordings : 2'),
(754, '-0.2', '2024-05-23 04:06:06', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 530859\nTotal Recordings : 2'),
(755, '-0.4', '2024-05-23 04:06:46', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 950945\nTotal Recordings : 4'),
(756, '-0.4', '2024-05-23 04:07:53', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 406756\nTotal Recordings : 4'),
(757, '-0.2', '2024-05-23 04:08:24', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 785797\nTotal Recordings : 2'),
(758, '-0.2', '2024-05-23 04:08:24', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 785797\nTotal Recordings : 2'),
(759, '-0.5', '2024-05-23 04:09:54', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 990016\nTotal Recordings : 5'),
(760, '-0.5', '2024-05-23 04:09:54', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 990016\nTotal Recordings : 5'),
(761, '-0.5', '2024-05-23 04:09:54', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 990016\nTotal Recordings : 5'),
(762, '0.2', '2024-05-23 04:18:15', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 785797 (Scheduled-1)\nUndelivered Recordings : 2'),
(763, '0.4', '2024-05-23 04:18:17', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 406756\nUndelivered Recordings : 4'),
(764, '-0.2', '2024-05-23 04:18:51', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 306152\nTotal Recordings : 2'),
(765, '-0.2', '2024-05-23 04:19:22', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 712447\nTotal Recordings : 2'),
(766, '-0.2', '2024-05-23 04:19:49', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 611897\nTotal Recordings : 2'),
(767, '-0.2', '2024-05-23 04:19:49', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 611897\nTotal Recordings : 2'),
(768, '-0.5', '2024-05-23 04:20:47', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 115266\nTotal Recordings : 5'),
(769, '-0.5', '2024-05-23 04:20:47', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 115266\nTotal Recordings : 5'),
(770, '-0.3', '2024-05-23 04:20:47', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 115266\nTotal Recordings : 3'),
(771, '-0.5', '2024-05-23 04:23:14', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 853432\nTotal Recordings : 5'),
(772, '-0.5', '2024-05-23 04:23:14', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 853432\nTotal Recordings : 5'),
(773, '-0.3', '2024-05-23 04:23:14', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 853432\nTotal Recordings : 3'),
(774, '-1.3', '2024-05-23 11:56:37', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 887993\nTotal Recordings : 13'),
(775, '-1.3', '2024-05-23 11:56:53', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 944425\nTotal Recordings : 13'),
(776, '-0.7', '2024-05-23 11:57:03', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 799520\nTotal Recordings : 7'),
(777, '-0.6', '2024-05-23 11:57:03', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 799520\nTotal Recordings : 6'),
(778, '-0.7', '2024-05-23 11:58:37', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 956001\nTotal Recordings : 7'),
(779, '-0.6', '2024-05-23 11:58:37', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 956001\nTotal Recordings : 6'),
(780, '0.7', '2024-06-12 02:07:22', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID : 956001 (Scheduled-1)\nUndelivered Recordings : 7'),
(781, '0', '2024-06-12 02:14:43', 14, 'Zhou Ming', 'mzhou9954@gmail.com', 'Order ID:956001 (Scheduled-2)  Ringless Voicemail:  Resent Credit Recordings:0');

-- --------------------------------------------------------

--
-- Table structure for table `voices`
--

CREATE TABLE `voices` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(200) CHARACTER SET utf8 NOT NULL DEFAULT 'New user',
  `url` varchar(255) CHARACTER SET utf8 NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `user_id` int(11) NOT NULL,
  `user_name` varchar(200) CHARACTER SET utf8 NOT NULL DEFAULT 'New user',
  `permission` int(11) NOT NULL DEFAULT 2,
  `active` int(11) NOT NULL DEFAULT 0,
  `admin_name` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT ''
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `voices`
--

INSERT INTO `voices` (`id`, `name`, `url`, `created_at`, `user_id`, `user_name`, `permission`, `active`, `admin_name`) VALUES
(589, 'Male Voice', 'https://ringlessvoicemail.leadmarketer.com/resources/alejandros.mp3', '2024-02-07 12:52:11', 1, 'Administrator', 0, 1, ''),
(581, 'Female Voice', 'https://videodrop.leadmarketer.com/default.mp3', '2024-02-07 06:56:46', 1, 'Administrator', 0, 1, ''),
(607, 'Xavier', 'https://videodrop.leadmarketer.com/default.mp3', '2024-02-29 03:14:09', 14, 'Zhou Ming', 1, 1, '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accounts`
--
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `account_group`
--
ALTER TABLE `account_group`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `clone_voices`
--
ALTER TABLE `clone_voices`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `contacts`
--
ALTER TABLE `contacts`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `contact_group`
--
ALTER TABLE `contact_group`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `cowboy_log`
--
ALTER TABLE `cowboy_log`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `groups`
--
ALTER TABLE `groups`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `ips`
--
ALTER TABLE `ips`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `mail_log`
--
ALTER TABLE `mail_log`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `purchase_history`
--
ALTER TABLE `purchase_history`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `recordings`
--
ALTER TABLE `recordings`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `scripts`
--
ALTER TABLE `scripts`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `signs`
--
ALTER TABLE `signs`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `usage_history`
--
ALTER TABLE `usage_history`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- Indexes for table `voices`
--
ALTER TABLE `voices`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `accounts`
--
ALTER TABLE `accounts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=71;

--
-- AUTO_INCREMENT for table `account_group`
--
ALTER TABLE `account_group`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=83;

--
-- AUTO_INCREMENT for table `clone_voices`
--
ALTER TABLE `clone_voices`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=638;

--
-- AUTO_INCREMENT for table `contacts`
--
ALTER TABLE `contacts`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=985;

--
-- AUTO_INCREMENT for table `contact_group`
--
ALTER TABLE `contact_group`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=973;

--
-- AUTO_INCREMENT for table `cowboy_log`
--
ALTER TABLE `cowboy_log`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=586;

--
-- AUTO_INCREMENT for table `groups`
--
ALTER TABLE `groups`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT for table `ips`
--
ALTER TABLE `ips`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=614;

--
-- AUTO_INCREMENT for table `mail_log`
--
ALTER TABLE `mail_log`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7692;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=803;

--
-- AUTO_INCREMENT for table `purchase_history`
--
ALTER TABLE `purchase_history`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=709;

--
-- AUTO_INCREMENT for table `recordings`
--
ALTER TABLE `recordings`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=562;

--
-- AUTO_INCREMENT for table `scripts`
--
ALTER TABLE `scripts`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=550;

--
-- AUTO_INCREMENT for table `signs`
--
ALTER TABLE `signs`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=530;

--
-- AUTO_INCREMENT for table `usage_history`
--
ALTER TABLE `usage_history`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=782;

--
-- AUTO_INCREMENT for table `voices`
--
ALTER TABLE `voices`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=638;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
