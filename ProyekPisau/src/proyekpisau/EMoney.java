/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyekpisau;

public class EMoney {
    protected String JenisEmoney;
    protected double saldo;
    protected boolean tersambung;
    
    public EMoney(String JenisEmoney){
        this.JenisEmoney = JenisEmoney;
        this.tersambung = false;
    }
    
    public void verifikasi(){
        System.out.println("Memulai koneksi dengan " + JenisEmoney);
    }
    
    public double getSaldo(){
        return saldo;
    }
    
    public String getJenisEmoney(){
        return JenisEmoney;
    }
    
    public boolean getStatus(){
        return tersambung;
    }
    
    public void setSaldo(double saldo){
        this.saldo = saldo;
    }
    
    public void cekSaldo(){
        System.out.println("Jumlah saldo di " + JenisEmoney + ": Rp " + saldo);
    }
}

class Mandiri extends EMoney {
    private String noRekMandiri;
    
    public Mandiri(String noRekMandiri){
        super("Mandiri");
        this.noRekMandiri = noRekMandiri;
    }
    
    @Override
    public void verifikasi(){
        System.out.println("Memulai koneksi " + JenisEmoney + " dengan nomor rekening " + noRekMandiri);
    }
}

class Gopay extends EMoney {
    private String noTelp;
    
    public Gopay(String noTelp){
        super("Gopay");
        this.noTelp = noTelp;
    }
    
    @Override
    public void verifikasi(){
        System.out.println("Memulai koneksi " + JenisEmoney + " dengan nomor telepon " + noTelp);
    }
}

class BCA extends EMoney {
    private String noRekBCA;
    
    public BCA(String noRekBCA){
        super("BCA");
        this.noRekBCA = noRekBCA;
    }
    
    @Override
    public void verifikasi(){
        System.out.println("Memulai koneksi " + JenisEmoney + " dengan nomor rekening " + noRekBCA);
    }
}


class Dana extends EMoney {
    private String noTelp;
    
    public Dana(String noTelp){
        super("Gopay");
        this.noTelp = noTelp;
    }
    
    @Override
    public void verifikasi(){
        System.out.println("Memulai koneksi " + JenisEmoney + " dengan nomor telepon " + noTelp);
    }
}
