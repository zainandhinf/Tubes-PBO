package state;

import javax.swing.JOptionPane;

import view.MainFrame; // Menggunakan MainFrame

/**
 * Kelas StateSiaga (Concrete State)
 * Merepresentasikan kondisi awal mesin ATM (Idle).
 * Pada state ini, mesin hanya menunggu pengguna memasukkan kartu.
 */
public class StateSiaga implements StateATM {

    @Override
    public void masukkanKartu(MesinATM atm, String nomorKartu) {
        System.out.println("[STATE] Kartu " + nomorKartu + " dimasukkan.");
        
        if (atm.getBankDatabase().isAkunExist(nomorKartu)) {
            
            // Jika Ada: Simpan nomor kartu di memori Mesin
            atm.setNomorKartuSementara(nomorKartu);
            
            // Pindah State
            atm.ubahState(new StateCekPin());
            
            // Ganti Layar
            if (atm.getJendelaUtama() instanceof MainFrame) {
                ((MainFrame) atm.getJendelaUtama()).gantiLayar("LOGIN");
            }
            
        } else {
            // Jika Tidak Ada: Tampilkan Error dan JANGAN pindah layar
            JOptionPane.showMessageDialog(null, 
                "Kartu tidak dikenali! Silakan coba nomor lain.", 
                "Error Kartu", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void masukkanPin(MesinATM atm, String pin) {
        // Tidak melakukan apa-apa di layar welcome
    }

    @Override public void pilihMenu(MesinATM atm, int pilihan) {}
    @Override public void prosesJumlah(MesinATM atm, double jumlah) {}
    @Override public void keluar(MesinATM atm) {}
}