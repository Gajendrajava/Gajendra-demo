-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Apr 09, 2026 at 01:05 PM
-- Server version: 5.7.24
-- PHP Version: 7.2.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `employeemanagement`
--

-- --------------------------------------------------------

--
-- Table structure for table `addemployees`
--

DROP TABLE IF EXISTS `addemployees`;
CREATE TABLE IF NOT EXISTS `addemployees` (
  `emp_id` int(11) NOT NULL,
  `emp_name` varchar(100) NOT NULL,
  `dob` date DEFAULT NULL,
  `contact` varchar(10) DEFAULT NULL,
  `dept` varchar(50) DEFAULT NULL,
  `position` varchar(50) DEFAULT NULL,
  `salary` double DEFAULT NULL,
  `join_date` date DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`emp_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `addemployees`
--

INSERT INTO `addemployees` (`emp_id`, `emp_name`, `dob`, `contact`, `dept`, `position`, `salary`, `join_date`, `address`, `email`) VALUES
(1101, 'suneelkumar', '2005-07-06', '9685472545', 'IT', 'Team Lead', 25050, '2025-06-02', 'sagar', 'suneel@gamil.com'),
(1102, 'prashant thakur', '2010-10-23', '9895265215', 'Finance', 'Receptionist', 20000, '2025-11-15', 'damoh', 'prashant@gmail.com'),
(1103, 'jitendra vishwakarma', '2006-10-23', '722406414', 'Sales', 'Clerk', 40000, '2025-11-18', 'bansa tarkheda', '@mailcom'),
(1107, 'Rituraj Sing Lodhi', '2007-12-20', '9198859855', 'HR', 'Team Lead', 20000, '2025-12-13', 'damoh', 'rituraj@144'),
(1105, 'Shrikant Patel', '2005-02-19', '636541255', 'Sales', 'Receptionist', 150000, '2026-04-02', 'Damoh', 'shrikant@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `add_attendance`
--

DROP TABLE IF EXISTS `add_attendance`;
CREATE TABLE IF NOT EXISTS `add_attendance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `emp_id` int(11) DEFAULT NULL,
  `emp_name` varchar(100) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `emp_id` (`emp_id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `add_attendance`
--

INSERT INTO `add_attendance` (`id`, `emp_id`, `emp_name`, `date`, `status`) VALUES
(1, 1101, 'suneelkumar', '2025-11-13', 'Present'),
(2, 1101, 'suneelkumar', '2025-11-14', 'Present'),
(3, 1101, 'suneelkumar', '2025-11-15', 'Present'),
(4, 1102, 'prashant thakur', '2025-11-15', 'Present'),
(5, 1101, 'suneelkumar', '2025-11-18', 'Absent'),
(6, 1103, 'jitendra vishwakarma', '2025-11-18', 'Present');

-- --------------------------------------------------------

--
-- Table structure for table `loginsignup`
--

DROP TABLE IF EXISTS `loginsignup`;
CREATE TABLE IF NOT EXISTS `loginsignup` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(250) NOT NULL,
  `Email` varchar(20) NOT NULL,
  `Contact` varchar(15) NOT NULL,
  `Address` varchar(25) NOT NULL,
  `UserName` varchar(25) NOT NULL,
  `Password` varchar(25) NOT NULL,
  `RePassword` varchar(100) NOT NULL,
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UserName` (`UserName`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `loginsignup`
--

INSERT INTO `loginsignup` (`ID`, `Name`, `Email`, `Contact`, `Address`, `UserName`, `Password`, `RePassword`, `CreatedAt`) VALUES
(1, 'Gajendra Singh Lodhi', 'gajendrathakur@01', '9691808508', 'damoh', 'gajju', 'thakur', 'thakur', '2025-11-10 07:45:16'),
(2, 'Shrikant Patel', 'Shrikant@gmail.com', '96857423', 'hindoria', 'patel', '1234', '1234', '2026-04-02 10:05:55');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
