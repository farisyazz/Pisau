
package proyekpisau;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String namaLengkap;
    private String username;
    private String password;
    private String email;
    private List<EMoney> listEMoney;
    private Laporan laporanUser;

    public User(String username, String password, String email, String namaLengkap) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.namaLengkap = namaLengkap;
        this.listEMoney = new ArrayList<>();
    }

    //getter
    public String getNamaLengkap() {
        return namaLengkap;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    
    public List<EMoney> getListEmoney() {
        return listEMoney;
    }

    //setter
    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLaporan(Laporan laporan) {
        this.laporanUser = laporan;
    }
    
    //others
    public void cetakLaporan(Laporan laporan) {
        laporan.detailLaporan(this.listEMoney);
    }

    public void tambahMetode(EMoney emoney) {
        emoney.verifikasi();
        listEMoney.add(emoney);
        System.out.println(emoney.getJenisEmoney() + " berhasil ditambahkan.");
    }

    public void tampilkanLaporanSaya() {
        if (this.laporanUser != null) {
            this.laporanUser.detailLaporan(this.listEMoney);
        } else {
            System.out.println("User " + namaLengkap + " belum memiliki laporan.");
        }
    }

    public double getTotalSaldo(){
        double total = 0;
        for (EMoney e : listEMoney){
            total += e.getSaldo();
        }
        return total;
    }
    
}
