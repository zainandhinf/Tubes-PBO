package state;

import javax.swing.JOptionPane;
import java.util.logging.Logger;
import service.ProxyAkun;
import util.Validator;
import view.MainFrame;

/**
 * Kelas StateMenuUtama
 * State aktif setelah user berhasil Login.
 * Menangani navigasi ke fitur-fitur lain berdasarkan pilihan menu.
 */
public class StateMenuUtama implements StateATM {
    private static final Logger LOGGER = Logger.getLogger(StateMenuUtama.class.getName());
    
    @Override
    public void pilihMenu(MesinATM atm, int pilihan) {
        Validator<Integer> menuValidator = new Validator<>(pilihan);

        // Rule: Menu hanya ada 1 sampai 6
        boolean isMenuValid = menuValidator.validate(i -> i >= 1 && i <= 6);

        if (!isMenuValid) {
            JOptionPane.showMessageDialog(null, 
                "Menu tidak tersedia! Pilih angka 1-6.", 
                "Input Invalid", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        MainFrame frame = (MainFrame) atm.getJendelaUtama();

        switch (pilihan) {
            case 1: // CEK SALDO
                LOGGER.info("[STATE] Pindah ke Cek Saldo");
                atm.ubahState(new StateCekSaldo());
                frame.gantiLayar("CEK_SALDO");
                break;

            case 2: // TARIK TUNAI
                LOGGER.info("[STATE] Pindah ke Tarik Tunai");
                atm.ubahState(new StateTarikTunai());
                frame.gantiLayar("TARIK_TUNAI");
                break;

            case 3: // SETOR TUNAI
                LOGGER.info("[STATE] Pindah ke Setor Tunai");
                atm.ubahState(new StateSetorTunai());
                frame.gantiLayar("SETOR_TUNAI");
                break;

            case 4: // TRANSFER
                LOGGER.info("[STATE] Pindah ke Transfer");
                atm.ubahState(new StateTransfer());
                frame.gantiLayar("TRANSFER");
                break;

            case 5: // RIWAYAT TRANSAKSI
                LOGGER.info("[STATE] Pindah ke Riwayat");
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

            LOGGER.info("[STATE] User Logout. Kembali ke Siaga.");
        }
    }

    // --- Method Tidak Terpakai di Menu Utama ---
    @Override
    public void masukkanKartu(MesinATM atm, String nomorKartu) {
        throw new UnsupportedOperationException(
            "Tidak dapat memasukkan kartu pada StateMenuUtama."
        );
    }

    @Override
    public void masukkanPin(MesinATM atm, String pin) {
        throw new UnsupportedOperationException(
            "Tidak dapat memasukkan PIN pada StateMenuUtama."
        );
    }

    @Override
    public void prosesJumlah(MesinATM atm, double jumlah) {
        throw new UnsupportedOperationException(
            "Tidak dapat memproses jumlah pada StateMenuUtama."
        );
    }

}