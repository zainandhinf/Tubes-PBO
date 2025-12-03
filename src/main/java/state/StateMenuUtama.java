package state;

import javax.swing.JOptionPane;
import service.ProxyAkun;
import view.MainFrame;

/**
 * Kelas StateMenuUtama
 * State aktif setelah user berhasil Login.
 * Menangani navigasi ke fitur-fitur lain berdasarkan pilihan menu.
 */
public class StateMenuUtama implements StateATM {

    @Override
    public void pilihMenu(MesinATM atm, int pilihan) {
        MainFrame frame = (MainFrame) atm.getJendelaUtama();

        switch (pilihan) {
            case 1: // CEK SALDO
                System.out.println("[STATE] Pindah ke Cek Saldo");
                atm.ubahState(new StateCekSaldo());
                frame.gantiLayar("CEK_SALDO");
                break;

            case 2: // TARIK TUNAI
                System.out.println("[STATE] Pindah ke Tarik Tunai");
                atm.ubahState(new StateTarikTunai());
                frame.gantiLayar("TARIK_TUNAI");
                break;

            case 3: // SETOR TUNAI
                System.out.println("[STATE] Pindah ke Setor Tunai");
                atm.ubahState(new StateSetorTunai());
                frame.gantiLayar("SETOR_TUNAI");
                break;

            case 4: // TRANSFER
                System.out.println("[STATE] Pindah ke Transfer");
                atm.ubahState(new StateTransfer());
                frame.gantiLayar("TRANSFER");
                break;

            case 5: // RIWAYAT TRANSAKSI
                System.out.println("[STATE] Pindah ke Riwayat");
                atm.ubahState(new StateRiwayat());
                frame.gantiLayar("RIWAYAT");
                break;

            case 6: // KELUAR (LOGOUT)
                keluar(atm);
                break;

            default:
                JOptionPane.showMessageDialog(null, "Menu tidak valid!");
        }
    }

    @Override
    public void keluar(MesinATM atm) {
        int confirm = JOptionPane.showConfirmDialog(null,
                "Apakah Anda yakin ingin keluar?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            ProxyAkun proxy = (ProxyAkun) atm.getProxy();
            proxy.logout();

            atm.ubahState(new StateSiaga());

            MainFrame frame = (MainFrame) atm.getJendelaUtama();
            frame.gantiLayar("WELCOME");

            System.out.println("[STATE] User Logout. Kembali ke Siaga.");
        }
    }

    // --- Method Tidak Terpakai di Menu Utama ---
    @Override
    public void masukkanKartu(MesinATM atm, String nomorKartu) {
    }

    @Override
    public void masukkanPin(MesinATM atm, String pin) {
    }

    @Override
    public void prosesJumlah(MesinATM atm, double jumlah) {
    }
}