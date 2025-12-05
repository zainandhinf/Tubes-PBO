package state;

/**
 * StateTransfer
 * State untuk proses transfer antar rekening pada ATM
 */
public class StateTransfer implements StateATM {
    @Override
    public void masukkanKartu(MesinATM atm, String nomorKartu) {
        throw new UnsupportedOperationException(
            "Tidak dapat memasukkan kartu pada mode transfer."
        );
    }

    @Override
    public void masukkanPin(MesinATM atm, String pin) {
        throw new UnsupportedOperationException(
            "Tidak dapat memasukkan PIN pada mode transfer."
        );
    }

    @Override
    public void pilihMenu(MesinATM atm, int pilihan) {
        throw new UnsupportedOperationException(
            "Tidak dapat memilih menu pada mode transfer."
        );
    }

    @Override
    public void prosesJumlah(MesinATM atm, double jumlah) {
        throw new UnsupportedOperationException(
            "Tidak dapat memproses jumlah di luar alur transfer."
        );
    }

    @Override
    public void keluar(MesinATM atm) {
        atm.ubahState(new StateMenuUtama());
    }

    public StateTransfer() {
        // Konstruktor kosong memang diperlukan untuk inisialisasi state
    }
}