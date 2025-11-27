
package state;

import javax.swing.JOptionPane;
import service.ProxyAkun;
import view.MainFrame;

/**
 * StateSetorTunai
 * State untuk proses setor tunai pada ATM
 */
public class StateSetorTunai implements StateATM {
    @Override
    public void masukkanKartu(MesinATM atm, String nomorKartu) {}

    @Override
    public void masukkanPin(MesinATM atm, String pin) {}

    @Override
    public void pilihMenu(MesinATM atm, int pilihan) {}

    @Override
    public void prosesJumlah(MesinATM atm, double jumlah) {}

    @Override
    public void keluar(MesinATM atm) {
        atm.ubahState(new StateMenuUtama());
    }

    // Fitur utama: setor tunai
    public StateSetorTunai() {}

    // Jalankan proses setor tunai saat state diaktifkan
    public void prosesSetor(MesinATM atm) {
        MainFrame gui = (MainFrame) atm.getJendelaUtama();
        ProxyAkun proxy = (ProxyAkun) atm.getProxy();
        String input = JOptionPane.showInputDialog(gui, "Masukkan nominal setor tunai:");
        if (input == null) {
            atm.ubahState(new StateMenuUtama());
            return;
        }
        try {
            // Hilangkan titik ribuan agar input seperti 100.000 dibaca 100000
            String cleanInput = input.replace(".", "").replace(",", "");
            double nominal = Double.parseDouble(cleanInput);
            if (nominal <= 0) {
                JOptionPane.showMessageDialog(gui, "Nominal harus lebih dari 0.");
            } else {
                proxy.setorTunai(nominal);
                JOptionPane.showMessageDialog(gui, "Setor tunai berhasil!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(gui, "Error: " + e.getMessage());
        }
        atm.ubahState(new StateMenuUtama());
    }
}
