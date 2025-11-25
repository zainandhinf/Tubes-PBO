package state;

/**
 * Interface StateATM
 * Mendefinisikan semua aksi yang mungkin dilakukan user di mesin ATM.
 * Setiap State (Layar) akan mengimplementasikan method ini dengan cara berbeda.
 */
public interface StateATM {
    
    void masukkanKartu(MesinATM atm, String nomorKartu);
    
    void masukkanPin(MesinATM atm, String pin);
    
    void pilihMenu(MesinATM atm, int pilihan);
    
    void prosesJumlah(MesinATM atm, double jumlah);
    
    void keluar(MesinATM atm);
}