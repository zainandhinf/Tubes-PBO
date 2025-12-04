package state;

import javax.swing.JOptionPane;
import java.util.logging.Logger;
import java.util.logging.Level;

import view.MainFrame;

/**
 * Kelas StateSiaga (Concrete State)
 * Merepresentasikan kondisi awal mesin ATM (Idle).
 * Pada state ini, mesin hanya menunggu pengguna memasukkan kartu.
 */
public class StateSiaga implements StateATM {
    private static final Logger LOGGER = Logger.getLogger(StateSiaga.class.getName());

    @Override
    public void masukkanKartu(MesinATM atm, String nomorKartu) {
        LOGGER.log(Level.INFO, "[STATE] Kartu {0} dimasukkan.", nomorKartu);
        
        if (atm.getBankDatabase().isAkunExist(nomorKartu)) {
            
            atm.setNomorKartuSementara(nomorKartu);
            
            atm.ubahState(new StateCekPin());
            
            if (atm.getJendelaUtama() instanceof MainFrame mainframe) {
                mainframe.gantiLayar("LOGIN");
            }
            
        } else {
            JOptionPane.showMessageDialog(null, 
                "Kartu tidak dikenali! Silakan coba nomor lain.", 
                "Error Kartu", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void masukkanPin(MesinATM atm, String pin) {
        // Pada state siaga, pengguna belum bisa memasukkan PIN
        throw new UnsupportedOperationException("Belum bisa memasukkan PIN pada StateSiaga.");
    }

    @Override
    public void pilihMenu(MesinATM atm, int pilihan) {
        // Tidak boleh memilih menu di state siaga
        throw new UnsupportedOperationException("Tidak bisa memilih menu pada StateSiaga.");
    }

    @Override
    public void prosesJumlah(MesinATM atm, double jumlah) {
        // Tidak ada pemrosesan jumlah pada state awal
        throw new UnsupportedOperationException("Tidak bisa memproses jumlah pada StateSiaga.");
    }
    
    @Override
    public void keluar(MesinATM atm) {
        // Keluar pada state siaga tidak melakukan apa-apa
        // Tetapi tetap diberi penjelasan agar SonarQube tidak protes
        // State ini memang idle; tidak ada kartu yang bisa dikeluarkan
        throw new UnsupportedOperationException("Tidak ada operasi keluar pada StateSiaga.");
    }
}