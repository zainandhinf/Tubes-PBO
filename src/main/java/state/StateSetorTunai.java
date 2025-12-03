
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
}
