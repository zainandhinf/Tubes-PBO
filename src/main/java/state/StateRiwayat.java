package state;

import view.MainFrame;

public class StateRiwayat implements StateATM {

    @Override
    public void masukkanKartu(MesinATM atm, String nomorKartu) {
        kembaliKeMenu(atm);
    }

    @Override
    public void masukkanPin(MesinATM atm, String pin) {
        kembaliKeMenu(atm);
    }

    @Override
    public void pilihMenu(MesinATM atm, int pilihan) {
        kembaliKeMenu(atm);
    }

    @Override
    public void prosesJumlah(MesinATM atm, double jumlah) {
        kembaliKeMenu(atm);
    }

    private void kembaliKeMenu(MesinATM atm) {
        atm.ubahState(new StateMenuUtama());
        if (atm.getJendelaUtama() instanceof MainFrame mainframe) {
            mainframe.gantiLayar("LOGIN");
        }
    }

    @Override
    public void keluar(MesinATM atm) {
        kembaliKeMenu(atm);
    }
}