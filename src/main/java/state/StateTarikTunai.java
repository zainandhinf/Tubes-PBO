package state;

import javax.swing.JOptionPane;
import service.ProxyAkun;
import view.MainFrame;

public class StateTarikTunai implements StateATM {

    @Override
    public void masukkanKartu(MesinATM atm, String nomorKartu) {
        // Tidak digunakan di state ini
    }

    @Override
    public void masukkanPin(MesinATM atm, String pin) {
        // Tidak digunakan di state ini
    }

    @Override
    public void pilihMenu(MesinATM atm, int pilihan) {
        // Tidak digunakan di state ini
    }

    @Override
    public void prosesJumlah(MesinATM atm, double jumlah) {
        MainFrame gui = (MainFrame) atm.getJendelaUtama();
        ProxyAkun proxy = (ProxyAkun) atm.getProxy();

        try {
            if (jumlah <= 0) {
                JOptionPane.showMessageDialog(gui, "Nominal harus lebih dari 0.");
            } else {
                proxy.tarikTunai(jumlah);
                JOptionPane.showMessageDialog(gui, "Tarik tunai berhasil!");
                atm.ubahState(new StateMenuUtama());
                gui.gantiLayar("MENU");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(gui, "Error: " + e.getMessage());
            // Tetap di layar tarik tunai atau kembali?
            // Untuk UX yang baik, biarkan user mencoba lagi atau cancel
        }
    }

    @Override
    public void keluar(MesinATM atm) {
        atm.ubahState(new StateMenuUtama());
        MainFrame gui = (MainFrame) atm.getJendelaUtama();
        gui.gantiLayar("MENU");
    }

    public StateTarikTunai() {
        // Konstruktor kosong
    }

    // Method prosesTarik lama dihapus karena digantikan oleh GUI Flow
    public void prosesTarik(MesinATM atm) {
        // Deprecated
    }
}
