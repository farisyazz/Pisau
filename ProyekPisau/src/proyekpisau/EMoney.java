package proyekpisau;

public class EMoney {
    protected int idUserEmoney;
    protected String JenisEmoney;
    protected int saldo;
    protected boolean tersambung;
    
    public EMoney(int idUserEmoney, String JenisEmoney, int saldo){
        this.idUserEmoney = idUserEmoney;
        this.JenisEmoney = JenisEmoney;
        this.saldo = saldo;
        this.tersambung = false;
    }
    
    public void verifikasi(){
        this.tersambung = true;
        // System.out.println("Starting connection with " + JenisEmoney);
    }
    
    //getter
    public int getIdUserEmoney(){
        return idUserEmoney;
    }

    public int getSaldo(){
        return saldo;
    }
    
    public String getJenisEmoney(){
        return JenisEmoney;
    }
    
    public boolean getStatus(){
        return tersambung;
    }
    
    //setter
    public void setSaldo(int saldo){
        this.saldo = saldo;
    }
}

class Mandiri extends EMoney {
    private String noRekMandiri;
    
    public Mandiri(int idUserEmoney, String noRekMandiri, int saldo){
        super(idUserEmoney, "Mandiri", saldo);
        this.noRekMandiri = noRekMandiri;
    }

    public String getNoRekMandiri(){
        return noRekMandiri;
    }
    
    @Override
    public void verifikasi(){
        this.tersambung = true;
        // System.out.println("Starting connection with " + JenisEmoney + " dengan nomor rekening " + noRekMandiri);
    }
}

class BCA extends EMoney {
    private String noRekBCA;
    
    public BCA(int idUserEmoney, String noRekBCA, int saldo){
        super(idUserEmoney, "BCA", saldo);
        this.noRekBCA = noRekBCA;
    }

    public String getNoRekBCA(){
        return noRekBCA;
    }
    
    @Override
    public void verifikasi(){
        this.tersambung = true;
        // System.out.println("Starting connection with " + JenisEmoney + " dengan nomor rekening " + noRekBCA);
    }
}

class Gopay extends EMoney {
    private String noTelp;
    
    public Gopay(int idUserEmoney, String noTelp, int saldo){
        super(idUserEmoney, "Gopay", saldo);
        this.noTelp = noTelp;
    }

    public String getnoTelp(){
        return noTelp;
    }
    
    @Override
    public void verifikasi(){
        this.tersambung = true;
        // System.out.println("Starting connection with " + JenisEmoney + " dengan nomor telepon " + noTelp);
    }
}

class Dana extends EMoney {
    private String noTelp;
    
    public Dana(int idUserEmoney, String noTelp, int saldo){
        super(idUserEmoney, "Dana", saldo);
        this.noTelp = noTelp;
    }

    public String getnoTelp(){
        return noTelp;
    }
    
    @Override
    public void verifikasi(){
        this.tersambung = true;
        // System.out.println("Starting connection with " + JenisEmoney + " dengan nomor telepon " + noTelp);
    }
}
