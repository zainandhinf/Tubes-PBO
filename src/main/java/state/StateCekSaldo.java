package state;

/**
 * Kelas StateCekSaldo
 * State aktif saat user memilih menu Cek Saldo.
 * Menampilkan saldo user di layar khusus.
 */
public class StateCekSaldo implements StateATM {

    @Override
    public void masukkanKartu(MesinATM atm, String nomorKartu) {
        // Tidak relevan di state ini
    }

    @Override
    public void masukkanPin(MesinATM atm, String pin) {
        // Tidak relevan di state ini
    }

    @Override
    public void pilihMenu(MesinATM atm, int pilihan) {
        // Tidak relevan di state ini
    }

    @Override
    public void prosesJumlah(MesinATM atm, double jumlah) {
        // Tidak relevan di state ini
    }

    @Override
    public void keluar(MesinATM atm) {
        // Kembali ke Menu Utama
        atm.ubahState(new StateMenuUtama());
    }
}
