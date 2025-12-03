package state;

import javax.swing.JOptionPane;
import service.ProxyAkun;
import view.MainFrame;

/**
 * StateTransfer
 * State untuk proses transfer antar rekening pada ATM
 */
public class StateTransfer implements StateATM {
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

    // Fitur utama: transfer
    public StateTransfer() {}
}
