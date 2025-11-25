package state;

import view.MainFrame; // Menggunakan MainFrame

/**
 * Kelas StateSiaga (Concrete State)
 * Merepresentasikan kondisi awal mesin ATM (Idle).
 * Pada state ini, mesin hanya menunggu pengguna memasukkan kartu.
 */
public class StateSiaga implements StateATM {

    @Override
    public void masukkanKartu(MesinATM atm, String nomorKartu) {
        System.out.println("[STATE] Kartu " + nomorKartu + " dimasukkan.");
        
        // 1. Ganti State ke Cek PIN
        atm.ubahState(new StateCekPin());
        
        // 2. Perintahkan GUI untuk ganti layar ke "LOGIN"
        // Pastikan di MesinATM method getter-nya mengembalikan MainFrame
        if (atm.getJendelaUtama() instanceof MainFrame) {
            ((MainFrame) atm.getJendelaUtama()).gantiLayar("LOGIN");
        }
    }

    @Override
    public void masukkanPin(MesinATM atm, String pin) {
        // Tidak melakukan apa-apa di layar welcome
    }

    @Override public void pilihMenu(MesinATM atm, int pilihan) {}
    @Override public void prosesJumlah(MesinATM atm, double jumlah) {}
    @Override public void keluar(MesinATM atm) {}
}