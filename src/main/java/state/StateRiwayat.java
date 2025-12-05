package state;

import view.MainFrame;

/**
 * Kelas StateRiwayat
 * State aktif saat user memilih menu Riwayat Transaksi (Mutasi).
 * Pada state ini, sistem menampilkan daftar transaksi terakhir pada layar kiri.
 * Interaksi input apapun dari keypad akan dianggap sebagai perintah "Kembali".
 */
public class StateRiwayat implements StateATM {

    /**
     * Mengabaikan input kartu dan kembali ke menu utama.
     */
    @Override
    public void masukkanKartu(MesinATM atm, String nomorKartu) {
        kembaliKeMenu(atm);
    }

    /**
     * Mengabaikan input PIN dan kembali ke menu utama.
     */
    @Override
    public void masukkanPin(MesinATM atm, String pin) {
        kembaliKeMenu(atm);
    }

    /**
     * Jika user menekan angka menu saat di layar riwayat,
     * sistem akan menganggapnya sebagai perintah kembali.
     */
    @Override
    public void pilihMenu(MesinATM atm, int pilihan) {
        kembaliKeMenu(atm);
    }

    /**
     * Jika user memasukkan angka nominal, sistem kembali ke menu.
     */
    @Override
    public void prosesJumlah(MesinATM atm, double jumlah) {
        kembaliKeMenu(atm);
    }

    /**
     * Helper method untuk menangani logika "Back" / Kembali.
     * Mengubah state kembali ke Menu Utama dan mengganti tampilan GUI.
     */
    private void kembaliKeMenu(MesinATM atm) {
        // 1. Kembalikan State ke Menu Utama
        atm.ubahState(new StateMenuUtama());
        
        // 2. Ganti Tampilan ke Dashboard Menu
        if (atm.getJendelaUtama() instanceof MainFrame mainframe) {
            // KOREKSI: Sebelumnya "LOGIN", diganti "MENU" agar kembali ke dashboard
            mainframe.gantiLayar("MENU");
        }
    }

    /**
     * Menangani tombol Keluar/Exit saat di layar riwayat.
     */
    @Override
    public void keluar(MesinATM atm) {
        kembaliKeMenu(atm);
    }
}