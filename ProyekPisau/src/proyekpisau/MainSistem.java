
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
    }
}
