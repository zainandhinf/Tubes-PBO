package model;

import java.util.logging.Logger;
import java.util.logging.Level;

public class AkunTabungan extends Akun {

    private static final Logger LOGGER = Logger.getLogger(AkunTabungan.class.getName());

    // Constructor
    public AkunTabungan(String noRek, String pin, double saldo, String tipeAkun) {
        super(noRek, pin, saldo, tipeAkun);
    }

    @Override
    public void infoAkun() {
        LOGGER.log(Level.INFO, "====================================");
        LOGGER.log(Level.INFO, "No. Rekening : {0}", this.getNoRek());
        LOGGER.log(Level.INFO, "Tipe Akun    : {0}", this.getTipeAkun());
        LOGGER.log(Level.INFO, "Sisa Saldo   : Rp {0}", this.getSaldo());
        LOGGER.log(Level.INFO, "====================================");
    }

}
