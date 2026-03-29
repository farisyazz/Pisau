/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyekpisau;

public class MainSistem {
    public static void main(String[] args){
        // 1. Inisialisasi User (Simulasi Registrasi & Login)
        User user1 = new User("mendez_12", "rahasia123", "Shawn Mendes");
        System.out.println("Sistem Manajemen Pembayaran Digital");
        System.out.println("Selamat Datang, " + user1.getNamaLengkap());
        System.out.println();

        // 2. Menambah Metode E-Money (Pilihan dari 4 metode yang tersedia)
        Mandiri mandiri1 = new Mandiri("141-00-1234567");
        Gopay gopay1 = new Gopay("0812-3456-7890");
        BCA bca1 = new BCA("882-099-1122");
        Dana dana1 = new Dana("0855-1111-2222");

        user1.tambahMetode(mandiri1);
        user1.tambahMetode(gopay1);
        user1.tambahMetode(bca1);
        user1.tambahMetode(dana1);
        System.out.println();

        // 3. Update Saldo (Simulasi Update Keuangan Otomatis dari Third Party)
        // System.out.println("=== SINKRONISASI DATA KEUANGAN ===");
        mandiri1.setSaldo(5000000);
        gopay1.setSaldo(150000);   
        bca1.setSaldo(2750000);    
        dana1.setSaldo(45000);     
        
        System.out.println("Data berhasil diperbarui dari E-Banking.");
        System.out.println();

        // Laporan Tahunan
        user1.setLaporan(new LaporanTahunan("2026-12-31", 2026));
        user1.tampilkanLaporanSaya();
        System.out.println();
        
        
        //--------
        User manda = new User("manda", "123rrrrr", "Amanda");
        System.out.println("Sistem Manajemen Pembayaran Digital");
        System.out.println("Selamat Datang, " + manda.getNamaLengkap());
        System.out.println();
        
        Mandiri mandiri2 = new Mandiri("141-00-1234569");
        Gopay gopay2 = new Gopay("0812-3456-7899");
        
        manda.tambahMetode(mandiri2);
        manda.tambahMetode(gopay2);
        System.out.println();
        
        mandiri2.setSaldo(5000);
        gopay2.setSaldo(20000);  
        
        System.out.println("Data berhasil diperbarui dari E-Banking.");
        System.out.println();

        // Laporan Bulanan
        manda.setLaporan(new LaporanBulanan("2026-12-31", "Mei"));
        manda.tampilkanLaporanSaya();
    }
}
