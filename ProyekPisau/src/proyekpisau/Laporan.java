/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyekpisau;
import java.util.List;

public class Laporan {
    protected String tanggalLaporan;
    protected double totalSaldo;
    
    public Laporan(String tanggalLaporan){
        this.tanggalLaporan = tanggalLaporan;
    }
    
    public void detailLaporan(List<EMoney> listEmoney){
        hitungTotalSaldo(listEmoney);
        for(EMoney emoney : listEmoney){
            System.out.println("Saldo " + emoney.getJenisEmoney() + ": Rp" + emoney.getSaldo());
        }
    }
    
    public double hitungTotalSaldo(List<EMoney> listEmoney){
        this.totalSaldo = 0;
        for(EMoney emoney : listEmoney){
            this.totalSaldo += emoney.getSaldo();
        }
        return this.totalSaldo;
    }
}

class LaporanBulanan extends Laporan {
    private String namaBulan;

    public LaporanBulanan(String tanggalLaporan, String namaBulan) {
        super(tanggalLaporan);
        this.namaBulan = namaBulan;
    }

    @Override
    public void detailLaporan(List<EMoney> listEmoney) {
        System.out.println("Laporan Bulanan: " + namaBulan.toUpperCase());
        System.out.println("Tanggal: " + tanggalLaporan);
        super.detailLaporan(listEmoney);
        System.out.println("Saldo saat ini: Rp" + totalSaldo);
    }
}

class LaporanTahunan extends Laporan {
    private int tahun;

    public LaporanTahunan(String tanggalLaporan, int tahun) {
        super(tanggalLaporan);
        this.tahun = tahun;
    }

    @Override
    public void detailLaporan(List<EMoney> listEmoney) {
        System.out.println("Laporan Tahunan: " + tahun);
        System.out.println("Tanggal: " + tanggalLaporan);
        super.detailLaporan(listEmoney);
        System.out.println("Total saldo terkumpul: Rp" + totalSaldo);
        System.out.println("Analisis: Kondisi keuangan Anda stabil tahun ini.");
    }
}
