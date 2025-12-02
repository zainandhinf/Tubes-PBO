package state;

import javax.swing.JOptionPane;
import service.ProxyAkun;
import view.MainFrame;

public class StateTarikTunai implements StateATM {

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

    public StateTarikTunai() {}

    // === PROSES UTAMA TARIK TUNAI ===
    public void prosesTarik(MesinATM atm) {
        MainFrame gui = (MainFrame) atm.getJendelaUtama();
        ProxyAkun proxy = (ProxyAkun) atm.getProxy();

        String input = JOptionPane.showInputDialog(gui, "Masukkan nominal tarik tunai:");

        // User menekan Cancel / close input box
        if (input == null) {
            atm.ubahState(new StateMenuUtama());
            return;
        }

        try {
            // Menghapus titik ribuan agar input seperti "100.000" dapat terbaca
            String cleanInput = input.replace(".", "").replace(",", "");
            double nominal = Double.parseDouble(cleanInput);

            if (nominal <= 0) {
                JOptionPane.showMessageDialog(gui, "Nominal harus lebih dari 0.");
            } else {
                proxy.tarikTunai(nominal);
                JOptionPane.showMessageDialog(gui, "Tarik tunai berhasil!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(gui, "Error: " + e.getMessage());
        }

        // Kembali ke menu utama setelah transaksi
        atm.ubahState(new StateMenuUtama());
    }
}
