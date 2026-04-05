
package proyekpisau;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;


public class MainSistem {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);

        // 1. Inisialisasi User (Simulasi Registrasi & Login)
        System.out.println("Selamat Datang di Sistem Manajeman Pembayaran Pisau <3");
        System.out.print("Punya akun? Yes(1)/No(0): ");
        int regis = input.nextInt();
        input.nextLine();
        if(regis == 1){
            System.out.print("Username: ");
            String inputUser = input.nextLine();
            System.out.print("Password: ");
            String inputPass = input.nextLine();

            try {
                Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sistem_pisau", "root", ""
                );

                // Query untuk mengecek apakah username dan password cocok
                String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, inputUser);
                ps.setString(2, inputPass);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    // Jika data ditemukan, buat objek User dari data database
                    User userAktif = new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("nama_lengkap")
                    );
                    
                    System.out.println("\nLogin Berhasil!");
                    System.out.println("Selamat Datang, " + userAktif.getNamaLengkap() + "!");
                } else {
                    System.out.println("\nLogin Gagal! Username atau Password salah.");
                }

                conn.close(); // Jangan lupa tutup koneksi
            } catch (Exception e) {
                System.out.println("Error Login: " + e.getMessage());
            }
        } 
        else {
            System.out.print("Nama Lengkap: ");
            String namaLengkap = input.nextLine();
            System.out.print("Username: ");
            String username = input.nextLine();
            System.out.print("Email: ");
            String email = input.nextLine();  
            System.out.print("Password: ");
            String password = input.nextLine(); 

            User user1 = new User(username, password, email, namaLengkap);   

            try {
                Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sistem_pisau", "root", ""
            );
            
                String sql = "INSERT INTO users (username, password, email, nama_lengkap) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                
                // ambil dari object
                ps.setString(1, user1.getUsername());
                ps.setString(2, user1.getPassword());
                ps.setString(3, user1.getEmail());
                ps.setString(4, user1.getNamaLengkap());
                
                
                ps.executeUpdate();
                
                System.out.println("Data berhasil disimpan!");
                
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }


        // User owner = new User("mendez_12", "rahasia123", "shawn@mail","Shawn Mendes");
        // System.out.println("Sistem Manajemen Pembayaran Digital");
        // System.out.println("Selamat Datang, " + owner.getNamaLengkap());
        // System.out.println();

        // // 2. Menambah Metode E-Money (Pilihan dari 4 metode yang tersedia)
        // Mandiri mandiri = new Mandiri("141-00-1234567");
        // Gopay gopay = new Gopay("0812-3456-7890");
        // BCA bca = new BCA("882-099-1122");
        // Dana dana = new Dana("0855-1111-2222");

        // owner.tambahMetode(mandiri);
        // owner.tambahMetode(gopay);
        // owner.tambahMetode(bca);
        // owner.tambahMetode(dana);
        // System.out.println();

        // // 3. Update Saldo (Simulasi Update Keuangan Otomatis dari Third Party)
        // // System.out.println("=== SINKRONISASI DATA KEUANGAN ===");
        // mandiri.setSaldo(5000000);
        // gopay.setSaldo(150000);   
        // bca.setSaldo(2750000);    
        // dana.setSaldo(45000);     
        
        // System.out.println("Data berhasil diperbarui dari E-Banking.");
        // System.out.println();

        // // Laporan Tahunan
        // owner.setLaporan(new LaporanTahunan("2026-12-31", 2026));
        // owner.tampilkanLaporanSaya();
        // System.out.println();
    }
}
