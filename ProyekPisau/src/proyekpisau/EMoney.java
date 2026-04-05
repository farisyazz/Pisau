
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
    
    //getter
    public double getSaldo(){
        return saldo;
    }
    
    public String getJenisEmoney(){
        return JenisEmoney;
    }
    
    public boolean getStatus(){
        return tersambung;
    }
    
    //setter
    public void setSaldo(double saldo){
        this.saldo = saldo;
    }
    
    //others
    // public void feeBulanan(){
    //     System.out.println("Tidak ada fee bulanan.");
    // }
}

class Mandiri extends EMoney {
    private String noRekMandiri;
    
    public Mandiri(String noRekMandiri){
        super("Mandiri");
        this.noRekMandiri = noRekMandiri;
    }

    public String getNoRekMandiri(){
        return noRekMandiri;
    }
    
    @Override
    public void verifikasi(){
        System.out.println("Memulai koneksi " + JenisEmoney + " dengan nomor rekening " + noRekMandiri);
    }
}

class BCA extends EMoney {
    private String noRekBCA;
    
    public BCA(String noRekBCA){
        super("BCA");
        this.noRekBCA = noRekBCA;
    }

    public String getNoRekBCA(){
        return noRekBCA;
    }
    
    @Override
    public void verifikasi(){
        System.out.println("Memulai koneksi " + JenisEmoney + " dengan nomor rekening " + noRekBCA);
    }
}

class Gopay extends EMoney {
    private String noTelp;
    
    public Gopay(String noTelp){
        super("Gopay");
        this.noTelp = noTelp;
    }

    public String getnoTelp(){
        return noTelp;
    }
    
    @Override
    public void verifikasi(){
        System.out.println("Memulai koneksi " + JenisEmoney + " dengan nomor telepon " + noTelp);
    }
}


class Dana extends EMoney {
    private String noTelp;
    
    public Dana(String noTelp){
        super("Gopay");
        this.noTelp = noTelp;
    }

    public String getnoTelp(){
        return noTelp;
    }
    
    @Override
    public void verifikasi(){
        System.out.println("Memulai koneksi " + JenisEmoney + " dengan nomor telepon " + noTelp);
    }
}
