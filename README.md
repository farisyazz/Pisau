# PISAU - Sistem Monitoring Laporan Keuangan Digital 💹

**PISAU** adalah aplikasi desktop berbasis **Java** yang dirancang untuk mempermudah pengguna dalam mengelola dan memantau berbagai akun e-money serta rekening bank dalam satu platform terpusat. Proyek ini berfokus pada kemudahan pemantauan saldo total dan transparansi riwayat transaksi keuangan digital.

---

# 👤 Identitas Pengembang
- Farisya Satya Utami (09021382429140)
- Hilya Rosidah (09021182429020)
- Adventha Dwi Cristia Putri (09021382429133)
- Amanda Alya Nur Afla (09021182429010)
- Enjelica Anggryan (09021382429127)
- M. Fadhil Pratama (09021282429121)
- Roitona Takaya (09021382429129)

---

# 🛠️ Teknologi (Tech Stack)
Aplikasi ini dibangun menggunakan teknologi berikut:
- **Bahasa Pemrograman:** Java (JDK 22+)
- **User Interface:** JavaFX (JFX) - Memberikan tampilan modern dan responsif.
- **Koneksi Database:** JDBC (Java Database Connectivity).
- **Storage/Database:** MySQL (dioperasikan melalui XAMPP).

---

## ✨ Fitur Utama
1. **Registrasi:** Pendaftaran akun baru bagi pengguna baru.
2. **Login:** Sistem autentikasi aman untuk masuk ke dashboard pribadi.
3. **Add Method/E-Wallet:** Menambahkan berbagai metode pembayaran digital (Gopay, Dana, Mandiri, BCA, dll).
4. **Delete Method/E-Wallet:** Menghapus atau memutuskan koneksi metode pembayaran yang tidak lagi digunakan.
5. **Log Laporan Transaksi:** - Pencatatan riwayat transaksi secara otomatis.
   - **Filtering System:** Pengguna dapat menyaring laporan berdasarkan **Bulan** dan **Jenis Wallet** tertentu untuk analisis keuangan yang lebih detail.

---

## 📂 Struktur Proyek
Proyek ini mengimplementasikan konsep *Object-Oriented Programming* (OOP) dengan struktur class sebagai berikut:

- **`Authentication`**: Menangani logika login dan registrasi pengguna.
- **`Dashboard`**: Tampilan utama yang merangkum saldo total dan navigasi cepat.
- **`ReportPage`**: Class khusus yang mengelola tampilan laporan transaksi beserta fitur filternya.
- **`User`**: Model data untuk menyimpan informasi profil pengguna.
- **`EMoney`**: Base class/model untuk berbagai jenis layanan e-wallet dan bank.

---

## ⚙️ Cara Menjalankan Proyek
1. Pastikan Anda telah menginstal **Java SDK** dan **JavaFX SDK**.
2. Jalankan kontrol panel **XAMPP** dan aktifkan layanan **MySQL**.
3. Import database `sistem_pisau.sql` (jika tersedia) ke phpMyAdmin.
4. Pastikan file library `mysql-connector-java.jar` sudah terhubung di project structure.
5. Jalankan `MainSistem.java` untuk memulai aplikasi.

---

*Proyek ini dikembangkan sebagai bagian dari tugas akhir mata kuliah Praktikum Pengembangan Perangkat Lunak Berorientasi Obyek pada program studi Teknik Informatika Universitas Sriwijaya.*


