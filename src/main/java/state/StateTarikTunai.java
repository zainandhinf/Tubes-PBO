package state;

import javax.swing.JOptionPane;
import service.ProxyAkun;
import view.MainFrame;
import util.Validator;

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

        Validator<Double> validator = new Validator<>(jumlah);

        // Rule: Harus Positif DAN Kelipatan 50.000
        boolean isValid = validator.validate(n -> n > 0 && n % 50000 == 0);

        if (!isValid) {
            JOptionPane.showMessageDialog(gui, 
                "Nominal harus lebih dari 0 dan kelipatan Rp 50.000", 
                "Input Invalid", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            proxy.tarikTunai(jumlah);
            
            JOptionPane.showMessageDialog(gui, "Tarik tunai berhasil!");
            
            // Kembali ke Menu Utama
            atm.ubahState(new StateMenuUtama());
            gui.gantiLayar("MENU");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(gui, "Error: " + e.getMessage(), "Transaksi Gagal", JOptionPane.ERROR_MESSAGE);
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
