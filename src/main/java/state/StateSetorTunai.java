
package state;

/**
 * StateSetorTunai
 * State untuk proses setor tunai pada ATM
 */
public class StateSetorTunai implements StateATM {
    @Override
    public void masukkanKartu(MesinATM atm, String nomorKartu) {
        throw new UnsupportedOperationException(
            "Tidak dapat memasukkan kartu pada mode setor tunai."
        );
    }

    @Override
    public void masukkanPin(MesinATM atm, String pin) {
        throw new UnsupportedOperationException(
            "Tidak dapat memasukkan PIN pada mode setor tunai."
        );
    }

    @Override
    public void pilihMenu(MesinATM atm, int pilihan) {
        throw new UnsupportedOperationException(
            "Tidak dapat memilih menu pada mode setor tunai."
        );
    }

    @Override
    public void prosesJumlah(MesinATM atm, double jumlah) {
        throw new UnsupportedOperationException(
            "Tidak dapat memproses jumlah di luar alur setor tunai."
        );
    }

    @Override
    public void keluar(MesinATM atm) {
        atm.ubahState(new StateMenuUtama());
    }

    public StateSetorTunai() {
        // Konstruktor kosong memang diperlukan untuk inisialisasi state
    }
}
