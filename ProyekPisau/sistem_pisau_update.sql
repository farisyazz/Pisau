-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 13 Apr 2026 pada 04.36
-- Versi server: 10.4.22-MariaDB
-- Versi PHP: 7.3.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sistem_pisau`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `jenis_emoney`
--

CREATE TABLE `jenis_emoney` (
  `id_jenis_emoney` int(11) NOT NULL,
  `nama_layanan` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `jenis_emoney`
--

INSERT INTO `jenis_emoney` (`id_jenis_emoney`, `nama_layanan`) VALUES
(1, 'Mandiri'),
(2, 'BCA'),
(3, 'Gopay'),
(4, 'Dana');

-- --------------------------------------------------------

--
-- Struktur dari tabel `log_transaksi`
--

CREATE TABLE `log_transaksi` (
  `id_transaksi` bigint(20) NOT NULL,
  `id_user_emoney` int(11) DEFAULT NULL,
  `tipe_transaksi` enum('MASUK','KELUAR') DEFAULT NULL,
  `jumlah` int(11) DEFAULT NULL,
  `waktu_transaksi` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `log_transaksi`
--

INSERT INTO `log_transaksi` (`id_transaksi`, `id_user_emoney`, `tipe_transaksi`, `jumlah`, `waktu_transaksi`) VALUES
(1, 1, 'MASUK', 50000, '2026-04-12 17:10:59'),
(2, 2, 'KELUAR', 10000, '2026-04-13 02:35:25');

--
-- Trigger `log_transaksi`
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
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id_user` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nama_lengkap` varchar(100) DEFAULT NULL,
  `email` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id_user`, `username`, `password`, `nama_lengkap`, `email`) VALUES
(1, 'mendezz', 'pass123', 'Shawn Mendes', 'shawn@gmail.com'),
(2, 'liamarchive', 'secret123', 'Lia M', 'liam@gmail.com');

-- --------------------------------------------------------

--
-- Struktur dari tabel `user_emoney`
--

CREATE TABLE `user_emoney` (
  `id_user_emoney` int(11) NOT NULL,
  `id_user` int(11) DEFAULT NULL,
  `id_jenis_emoney` int(11) DEFAULT NULL,
  `nomor_identitas` varchar(50) DEFAULT NULL,
  `saldo` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `user_emoney`
--

INSERT INTO `user_emoney` (`id_user_emoney`, `id_user`, `id_jenis_emoney`, `nomor_identitas`, `saldo`) VALUES
(1, 2, 1, '1200019800313', 1050000),
(2, 2, 4, '089604338348', 90000),
(3, 2, 3, '089604338348', 250000);

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `jenis_emoney`
--
ALTER TABLE `jenis_emoney`
  ADD PRIMARY KEY (`id_jenis_emoney`);

--
-- Indeks untuk tabel `log_transaksi`
--
ALTER TABLE `log_transaksi`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD KEY `waktu_transaksi` (`waktu_transaksi`),
  ADD KEY `id_user_emoney` (`id_user_emoney`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indeks untuk tabel `user_emoney`
--
ALTER TABLE `user_emoney`
  ADD PRIMARY KEY (`id_user_emoney`),
  ADD KEY `id_user` (`id_user`),
  ADD KEY `id_jenis_emoney` (`id_jenis_emoney`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `jenis_emoney`
--
ALTER TABLE `jenis_emoney`
  MODIFY `id_jenis_emoney` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT untuk tabel `log_transaksi`
--
ALTER TABLE `log_transaksi`
  MODIFY `id_transaksi` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT untuk tabel `user_emoney`
--
ALTER TABLE `user_emoney`
  MODIFY `id_user_emoney` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `log_transaksi`
--
ALTER TABLE `log_transaksi`
  ADD CONSTRAINT `log_transaksi_ibfk_1` FOREIGN KEY (`id_user_emoney`) REFERENCES `user_emoney` (`id_user_emoney`);

--
-- Ketidakleluasaan untuk tabel `user_emoney`
--
ALTER TABLE `user_emoney`
  ADD CONSTRAINT `user_emoney_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`),
  ADD CONSTRAINT `user_emoney_ibfk_2` FOREIGN KEY (`id_jenis_emoney`) REFERENCES `jenis_emoney` (`id_jenis_emoney`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
