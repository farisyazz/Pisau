/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyekpisau;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String namaLengkap;
    private String username;
    private String password;
    private List<EMoney> listEMoney;
    private Laporan laporanUser;

    public User(String username, String password, String namaLengkap) {
        this.username = username;
        this.password = password;
        this.namaLengkap = namaLengkap;
        this.listEMoney = new ArrayList<>();
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public String getUsername() {
        return username;
    }
    
    public List<EMoney> getListEmoney() {
        return listEMoney;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public void cetakLaporan(Laporan laporan) {
        laporan.detailLaporan(this.listEMoney);
    }

    public void tambahMetode(EMoney emoney) {
        emoney.verifikasi();
        listEMoney.add(emoney);
        System.out.println(emoney.getJenisEmoney() + " berhasil ditambahkan.");
    }

    public void setLaporan(Laporan laporan) {
        this.laporanUser = laporan;
    }

    public void tampilkanLaporanSaya() {
        if (this.laporanUser != null) {
            this.laporanUser.detailLaporan(this.listEMoney);
        } else {
            System.out.println("User " + namaLengkap + " belum memiliki laporan.");
        }
    }
    
}
