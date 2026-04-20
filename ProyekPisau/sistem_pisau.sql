-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 20, 2026 at 04:28 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `projectpisau`
--

-- --------------------------------------------------------

--
-- Table structure for table `jenis_emoney`
--

CREATE TABLE `jenis_emoney` (
  `id_jenis_emoney` int(11) NOT NULL,
  `nama_layanan` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `jenis_emoney`
--

INSERT INTO `jenis_emoney` (`id_jenis_emoney`, `nama_layanan`) VALUES
(1, 'Mandiri'),
(2, 'BCA'),
(3, 'Gopay'),
(4, 'Dana');

-- --------------------------------------------------------

--
-- Table structure for table `log_transaksi`
--

CREATE TABLE `log_transaksi` (
  `id_transaksi` bigint(20) NOT NULL,
  `id_user_emoney` int(11) DEFAULT NULL,
  `tipe_transaksi` enum('MASUK','KELUAR') DEFAULT NULL,
  `jumlah` int(11) DEFAULT NULL,
  `waktu_transaksi` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `log_transaksi`
--

INSERT INTO `log_transaksi` (`id_transaksi`, `id_user_emoney`, `tipe_transaksi`, `jumlah`, `waktu_transaksi`) VALUES
(1, 1, 'MASUK', 50000, '2026-04-12 17:10:59'),
(2, 2, 'KELUAR', 10000, '2026-04-13 02:35:25'),
(4, 1, 'MASUK', 100000, '2026-04-13 06:04:21'),
(5, 3, 'KELUAR', 50000, '2026-04-13 06:08:23'),
(6, 1, 'MASUK', 50000, '2026-04-17 08:52:48'),
(7, 1, 'KELUAR', 50000, '2026-04-17 08:56:53'),
(8, 1, 'MASUK', 300000, '2026-04-20 02:28:09'),
(9, 1, 'MASUK', 2500000, '2026-01-01 02:00:00'),
(10, 1, 'KELUAR', 50000, '2026-01-02 05:30:00'),
(11, 1, 'KELUAR', 15000, '2026-01-03 12:45:00'),
(12, 1, 'KELUAR', 12500, '2026-01-05 00:15:00'),
(13, 1, 'KELUAR', 120000, '2026-01-07 07:00:00'),
(14, 1, 'KELUAR', 35000, '2026-01-08 14:10:00'),
(15, 1, 'KELUAR', 22000, '2026-01-10 06:20:00'),
(16, 1, 'KELUAR', 55000, '2026-01-12 11:30:00'),
(17, 1, 'KELUAR', 15000, '2026-01-15 02:00:00'),
(18, 1, 'KELUAR', 40000, '2026-01-18 13:00:00'),
(19, 1, 'MASUK', 500000, '2026-02-01 03:00:00'),
(20, 1, 'KELUAR', 25000, '2026-02-03 05:00:00'),
(21, 1, 'KELUAR', 85000, '2026-02-05 08:45:00'),
(22, 1, 'KELUAR', 12000, '2026-02-10 01:00:00'),
(23, 1, 'KELUAR', 30000, '2026-02-15 12:00:00'),
(24, 1, 'MASUK', 500000, '2026-03-01 02:00:00'),
(25, 1, 'KELUAR', 45000, '2026-03-05 04:30:00'),
(26, 1, 'KELUAR', 15000, '2026-03-10 10:00:00'),
(27, 1, 'KELUAR', 120000, '2026-03-25 07:00:00'),
(28, 1, 'MASUK', 500000, '2026-04-01 03:00:00'),
(29, 1, 'KELUAR', 25000, '2026-04-05 01:00:00'),
(30, 1, 'KELUAR', 10000, '2026-04-10 05:00:00'),
(31, 1, 'KELUAR', 50000, '2026-04-15 12:00:00'),
(32, 2, 'MASUK', 3000000, '2026-01-01 03:00:00'),
(33, 2, 'KELUAR', 250000, '2026-01-02 03:00:00'),
(34, 2, 'KELUAR', 15000, '2026-01-05 07:20:00'),
(35, 2, 'KELUAR', 45000, '2026-01-10 01:30:00'),
(36, 2, 'KELUAR', 12000, '2026-01-15 12:00:00'),
(37, 2, 'KELUAR', 85000, '2026-01-20 05:00:00'),
(38, 2, 'MASUK', 1500000, '2026-02-01 02:00:00'),
(39, 2, 'KELUAR', 20000, '2026-02-05 08:00:00'),
(40, 2, 'KELUAR', 100000, '2026-02-12 04:00:00'),
(41, 2, 'KELUAR', 35000, '2026-02-20 11:00:00'),
(42, 2, 'MASUK', 1500000, '2026-03-01 03:00:00'),
(43, 2, 'KELUAR', 50000, '2026-03-10 02:00:00'),
(44, 2, 'KELUAR', 15000, '2026-03-15 13:00:00'),
(45, 2, 'KELUAR', 22000, '2026-03-20 06:00:00'),
(46, 2, 'MASUK', 1500000, '2026-04-01 01:00:00'),
(47, 2, 'KELUAR', 12500, '2026-04-05 07:00:00'),
(48, 2, 'KELUAR', 60000, '2026-04-12 12:00:00'),
(49, 3, 'MASUK', 1000000, '2026-01-01 04:00:00'),
(50, 3, 'KELUAR', 12000, '2026-01-02 00:00:00'),
(51, 3, 'KELUAR', 15000, '2026-01-03 00:15:00'),
(52, 3, 'KELUAR', 10000, '2026-01-04 00:10:00'),
(53, 3, 'KELUAR', 25000, '2026-01-06 05:00:00'),
(54, 3, 'KELUAR', 15000, '2026-01-08 00:00:00'),
(55, 3, 'KELUAR', 12000, '2026-01-10 09:00:00'),
(56, 3, 'MASUK', 500000, '2026-02-01 02:00:00'),
(57, 3, 'KELUAR', 20000, '2026-02-05 00:30:00'),
(58, 3, 'KELUAR', 15000, '2026-02-15 05:45:00'),
(59, 3, 'MASUK', 500000, '2026-03-01 04:00:00'),
(60, 3, 'KELUAR', 10000, '2026-03-05 00:00:00'),
(61, 3, 'KELUAR', 45000, '2026-03-20 08:00:00'),
(62, 4, 'MASUK', 4000000, '2026-01-01 01:00:00'),
(63, 4, 'KELUAR', 150000, '2026-01-02 03:15:00'),
(64, 4, 'KELUAR', 12000, '2026-01-03 11:30:00'),
(65, 4, 'KELUAR', 25000, '2026-01-05 00:45:00'),
(66, 4, 'KELUAR', 85000, '2026-01-07 05:20:00'),
(67, 4, 'KELUAR', 15000, '2026-01-08 14:00:00'),
(68, 4, 'KELUAR', 22500, '2026-01-10 06:00:00'),
(69, 4, 'KELUAR', 50000, '2026-01-12 12:15:00'),
(70, 4, 'KELUAR', 12000, '2026-01-14 01:00:00'),
(71, 4, 'KELUAR', 200000, '2026-01-15 08:30:00'),
(72, 4, 'KELUAR', 35000, '2026-01-18 03:00:00'),
(73, 4, 'KELUAR', 18000, '2026-01-20 10:45:00'),
(74, 4, 'KELUAR', 65000, '2026-01-22 07:00:00'),
(75, 4, 'KELUAR', 12000, '2026-01-25 02:30:00'),
(76, 4, 'KELUAR', 40000, '2026-01-28 13:00:00'),
(77, 4, 'MASUK', 1000000, '2026-02-01 02:00:00'),
(78, 4, 'KELUAR', 15000, '2026-02-03 05:00:00'),
(79, 4, 'KELUAR', 125000, '2026-02-05 09:30:00'),
(80, 4, 'KELUAR', 30000, '2026-02-08 01:15:00'),
(81, 4, 'KELUAR', 22000, '2026-02-12 12:00:00'),
(82, 4, 'KELUAR', 15000, '2026-02-15 06:00:00'),
(83, 4, 'KELUAR', 55000, '2026-02-20 14:00:00'),
(84, 4, 'MASUK', 1000000, '2026-03-01 03:00:00'),
(85, 4, 'KELUAR', 85000, '2026-03-05 04:00:00'),
(86, 4, 'KELUAR', 12000, '2026-03-10 10:30:00'),
(87, 4, 'KELUAR', 45000, '2026-03-15 07:00:00'),
(88, 4, 'KELUAR', 15000, '2026-03-20 02:00:00'),
(89, 4, 'KELUAR', 100000, '2026-03-25 12:00:00'),
(90, 4, 'MASUK', 1000000, '2026-04-01 01:00:00'),
(91, 4, 'KELUAR', 35000, '2026-04-05 05:00:00'),
(92, 4, 'KELUAR', 22000, '2026-04-10 11:00:00'),
(93, 4, 'KELUAR', 15000, '2026-04-15 00:00:00'),
(94, 4, 'KELUAR', 50000, '2026-04-19 13:30:00'),
(95, 5, 'MASUK', 3500000, '2026-01-01 04:00:00'),
(96, 5, 'KELUAR', 45000, '2026-01-02 07:00:00'),
(97, 5, 'KELUAR', 12500, '2026-01-04 03:00:00'),
(98, 5, 'KELUAR', 60000, '2026-01-06 12:30:00'),
(99, 5, 'KELUAR', 35000, '2026-01-08 01:00:00'),
(100, 5, 'KELUAR', 150000, '2026-01-11 06:00:00'),
(101, 5, 'KELUAR', 12000, '2026-01-13 10:00:00'),
(102, 5, 'KELUAR', 25000, '2026-01-15 02:15:00'),
(103, 5, 'KELUAR', 85000, '2026-01-18 13:45:00'),
(104, 5, 'KELUAR', 15000, '2026-01-21 05:00:00'),
(105, 5, 'KELUAR', 40000, '2026-01-24 08:30:00'),
(106, 5, 'KELUAR', 22000, '2026-01-27 12:00:00'),
(107, 5, 'KELUAR', 10000, '2026-01-30 01:30:00'),
(108, 5, 'MASUK', 1500000, '2026-02-01 03:00:00'),
(109, 5, 'KELUAR', 55000, '2026-02-04 07:00:00'),
(110, 5, 'KELUAR', 15000, '2026-02-08 02:00:00'),
(111, 5, 'KELUAR', 120000, '2026-02-12 11:00:00'),
(112, 5, 'KELUAR', 35000, '2026-02-16 04:00:00'),
(113, 5, 'KELUAR', 12000, '2026-02-20 08:00:00'),
(114, 5, 'KELUAR', 45000, '2026-02-25 13:00:00'),
(115, 5, 'MASUK', 1500000, '2026-03-01 02:00:00'),
(116, 5, 'KELUAR', 25000, '2026-03-04 05:30:00'),
(117, 5, 'KELUAR', 15000, '2026-03-08 01:00:00'),
(118, 5, 'KELUAR', 75000, '2026-03-12 12:00:00'),
(119, 5, 'KELUAR', 30000, '2026-03-18 07:00:00'),
(120, 5, 'KELUAR', 10000, '2026-03-24 03:00:00'),
(121, 5, 'MASUK', 1500000, '2026-04-01 04:00:00'),
(122, 5, 'KELUAR', 60000, '2026-04-05 06:00:00'),
(123, 5, 'KELUAR', 22000, '2026-04-12 11:00:00'),
(124, 5, 'KELUAR', 15000, '2026-04-18 02:00:00'),
(125, 6, 'MASUK', 5000000, '2026-01-01 00:00:00'),
(126, 6, 'KELUAR', 500000, '2026-01-02 02:00:00'),
(127, 6, 'KELUAR', 12000, '2026-01-05 06:00:00'),
(128, 6, 'KELUAR', 35000, '2026-01-07 11:00:00'),
(129, 6, 'KELUAR', 15000, '2026-01-10 01:00:00'),
(130, 6, 'KELUAR', 25000, '2026-01-12 05:30:00'),
(131, 6, 'KELUAR', 85000, '2026-01-15 12:00:00'),
(132, 6, 'KELUAR', 12000, '2026-01-18 00:45:00'),
(133, 6, 'KELUAR', 40000, '2026-01-20 08:00:00'),
(134, 6, 'KELUAR', 22000, '2026-01-22 13:00:00'),
(135, 6, 'KELUAR', 15000, '2026-01-25 04:00:00'),
(136, 6, 'KELUAR', 100000, '2026-01-28 07:00:00'),
(137, 6, 'MASUK', 2000000, '2026-02-01 01:00:00'),
(138, 6, 'KELUAR', 12500, '2026-02-05 03:00:00'),
(139, 6, 'KELUAR', 60000, '2026-02-10 11:00:00'),
(140, 6, 'KELUAR', 15000, '2026-02-15 01:00:00'),
(141, 6, 'KELUAR', 35000, '2026-02-20 05:00:00'),
(142, 6, 'MASUK', 2000000, '2026-03-01 00:30:00'),
(143, 6, 'KELUAR', 45000, '2026-03-05 12:00:00'),
(144, 6, 'KELUAR', 12000, '2026-03-12 04:00:00'),
(145, 6, 'KELUAR', 15000, '2026-03-18 02:00:00'),
(146, 6, 'KELUAR', 75000, '2026-03-25 07:00:00'),
(147, 6, 'MASUK', 2000000, '2026-04-01 01:30:00'),
(148, 6, 'KELUAR', 25000, '2026-04-06 06:00:00'),
(149, 6, 'KELUAR', 10000, '2026-04-12 11:00:00'),
(150, 6, 'KELUAR', 50000, '2026-04-18 03:00:00'),
(151, 7, 'MASUK', 5000000, '2026-01-01 03:00:00'),
(152, 7, 'KELUAR', 450000, '2026-01-05 04:00:00'),
(153, 7, 'KELUAR', 15000, '2026-01-15 07:30:00'),
(154, 7, 'KELUAR', 22000, '2026-01-25 12:00:00'),
(155, 7, 'MASUK', 2000000, '2026-02-01 02:00:00'),
(156, 7, 'KELUAR', 550000, '2026-02-04 03:20:00'),
(157, 7, 'KELUAR', 12000, '2026-02-18 09:00:00'),
(158, 7, 'MASUK', 2000000, '2026-03-01 03:00:00'),
(159, 7, 'KELUAR', 480000, '2026-03-05 06:45:00'),
(160, 7, 'KELUAR', 25000, '2026-03-20 02:00:00'),
(161, 7, 'MASUK', 2000000, '2026-04-01 01:00:00'),
(162, 7, 'KELUAR', 520000, '2026-04-04 05:00:00'),
(163, 7, 'KELUAR', 15000, '2026-04-15 11:30:00'),
(164, 8, 'MASUK', 4500000, '2026-01-01 01:30:00'),
(165, 8, 'KELUAR', 25000, '2026-01-02 00:15:00'),
(166, 8, 'KELUAR', 15000, '2026-01-02 05:00:00'),
(167, 8, 'KELUAR', 35000, '2026-01-03 12:00:00'),
(168, 8, 'KELUAR', 12000, '2026-01-04 01:20:00'),
(169, 8, 'KELUAR', 60000, '2026-01-05 06:00:00'),
(170, 8, 'KELUAR', 10000, '2026-01-07 10:45:00'),
(171, 8, 'KELUAR', 22000, '2026-01-09 04:30:00'),
(172, 8, 'KELUAR', 15000, '2026-01-12 02:00:00'),
(173, 8, 'KELUAR', 45000, '2026-01-15 13:15:00'),
(174, 8, 'KELUAR', 12500, '2026-01-20 07:00:00'),
(175, 8, 'KELUAR', 30000, '2026-01-25 11:30:00'),
(176, 8, 'MASUK', 1500000, '2026-02-01 02:00:00'),
(177, 8, 'KELUAR', 20000, '2026-02-02 01:00:00'),
(178, 8, 'KELUAR', 15000, '2026-02-05 05:30:00'),
(179, 8, 'KELUAR', 75000, '2026-02-08 12:00:00'),
(180, 8, 'KELUAR', 12000, '2026-02-12 03:00:00'),
(181, 8, 'KELUAR', 35000, '2026-02-15 08:00:00'),
(182, 8, 'KELUAR', 50000, '2026-02-20 14:00:00'),
(183, 8, 'MASUK', 1500000, '2026-03-01 03:00:00'),
(184, 8, 'KELUAR', 25000, '2026-03-02 02:15:00'),
(185, 8, 'KELUAR', 15000, '2026-03-05 05:45:00'),
(186, 8, 'KELUAR', 40000, '2026-03-10 11:00:00'),
(187, 8, 'KELUAR', 12000, '2026-03-15 01:30:00'),
(188, 8, 'KELUAR', 100000, '2026-03-20 07:00:00'),
(189, 8, 'KELUAR', 22000, '2026-03-25 12:30:00'),
(190, 8, 'MASUK', 1500000, '2026-04-01 01:00:00'),
(191, 8, 'KELUAR', 35000, '2026-04-05 04:00:00'),
(192, 8, 'KELUAR', 15000, '2026-04-10 10:00:00'),
(193, 8, 'KELUAR', 50000, '2026-04-18 13:00:00'),
(194, 8, 'KELUAR', 12500, '2026-04-19 08:30:00');

--
-- Triggers `log_transaksi`
--
DELIMITER $$
CREATE TRIGGER `update_saldo_after_log` AFTER INSERT ON `log_transaksi` FOR EACH ROW BEGIN
    IF NEW.tipe_transaksi = 'MASUK' THEN
        UPDATE user_emoney 
        SET saldo = saldo + NEW.jumlah 
        WHERE id_user_emoney = NEW.id_user_emoney;
    ELSEIF NEW.tipe_transaksi = 'KELUAR' THEN
        UPDATE user_emoney 
        SET saldo = saldo - NEW.jumlah 
        WHERE id_user_emoney = NEW.id_user_emoney;
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id_user` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nama_lengkap` varchar(100) DEFAULT NULL,
  `email` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id_user`, `username`, `password`, `nama_lengkap`, `email`) VALUES
(1, 'mendezz', 'pass123', 'Shawn Mendes', 'shawn@gmail.com'),
(2, 'liamarchive', 'secret123', 'Lia M', 'liam@gmail.com'),
(3, 'fatson', 'todd23', 'Jason Todd', 'jasont@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `user_emoney`
--

CREATE TABLE `user_emoney` (
  `id_user_emoney` int(11) NOT NULL,
  `id_user` int(11) DEFAULT NULL,
  `id_jenis_emoney` int(11) DEFAULT NULL,
  `nomor_identitas` varchar(50) DEFAULT NULL,
  `pass_emoney` varchar(100) DEFAULT NULL,
  `saldo` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_emoney`
--

INSERT INTO `user_emoney` (`id_user_emoney`, `id_user`, `id_jenis_emoney`, `nomor_identitas`, `pass_emoney`, `saldo`) VALUES
(1, 2, 1, '1200019800313', '19800313', 4368500),
(2, 2, 4, '089604338348', '38348', 6868500),
(3, 2, 3, '089604338348', '89604', 2021000),
(4, NULL, 2, '8897921302141', '02141', 6617500),
(5, NULL, 3, '083255673835', '73835', 7954500),
(6, NULL, 1, '1230076112980', '112980', 10784500),
(7, NULL, 2, '230006711207', '11207', 9911000),
(8, NULL, 3, '082173419820', '19820', 9185000);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `jenis_emoney`
--
ALTER TABLE `jenis_emoney`
  ADD PRIMARY KEY (`id_jenis_emoney`);

--
-- Indexes for table `log_transaksi`
--
ALTER TABLE `log_transaksi`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD KEY `waktu_transaksi` (`waktu_transaksi`),
  ADD KEY `id_user_emoney` (`id_user_emoney`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `user_emoney`
--
ALTER TABLE `user_emoney`
  ADD PRIMARY KEY (`id_user_emoney`),
  ADD KEY `id_user` (`id_user`),
  ADD KEY `id_jenis_emoney` (`id_jenis_emoney`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `jenis_emoney`
--
ALTER TABLE `jenis_emoney`
  MODIFY `id_jenis_emoney` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `log_transaksi`
--
ALTER TABLE `log_transaksi`
  MODIFY `id_transaksi` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=195;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `user_emoney`
--
ALTER TABLE `user_emoney`
  MODIFY `id_user_emoney` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `log_transaksi`
--
ALTER TABLE `log_transaksi`
  ADD CONSTRAINT `log_transaksi_ibfk_1` FOREIGN KEY (`id_user_emoney`) REFERENCES `user_emoney` (`id_user_emoney`);

--
-- Constraints for table `user_emoney`
--
ALTER TABLE `user_emoney`
  ADD CONSTRAINT `user_emoney_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`),
  ADD CONSTRAINT `user_emoney_ibfk_2` FOREIGN KEY (`id_jenis_emoney`) REFERENCES `jenis_emoney` (`id_jenis_emoney`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
