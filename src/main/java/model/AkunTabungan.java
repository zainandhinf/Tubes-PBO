package model;

public class AkunTabungan extends Akun {

    // Constructor
    public AkunTabungan(String noRek, String pin, double saldo) {
        super(noRek, pin, saldo);
    }

    @Override
    public void infoAkun() {
        System.out.println("====================================");
        System.out.println("No. Rekening: " + this.getNoRek());
        System.out.println("====================================");
    }

}
