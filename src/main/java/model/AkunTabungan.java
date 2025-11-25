package model;

public class AkunTabungan extends Akun {

    // Constructor
    public AkunTabungan(String noRek, String pin, double saldo, String tipeAkun) {
        super(noRek, pin, saldo, tipeAkun);
    }

    @Override
    public void infoAkun() {
        System.out.println("====================================");
        System.out.println("No. Rekening : " + this.getNoRek());
        System.out.println("Tipe Akun    : " + this.getTipeAkun());
        System.out.println("Sisa Saldo   : Rp " + this.getSaldo());
        System.out.println("====================================");
    }

}
