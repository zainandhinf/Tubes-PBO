package state;

import javax.swing.JOptionPane;
import model.Akun;
import service.ProxyAkun;
import java.util.logging.Logger;

/**
 * Kelas StateCekPin (Concrete State)
 * Merepresentasikan kondisi mesin ATM saat sedang menunggu input PIN dari user.
 * Bertugas menghubungkan TampilanLogin (View) dengan ProxyAkun (Security).
 */
public class StateCekPin implements StateATM {
    private static final Logger LOGGER = Logger.getLogger(StateCekPin.class.getName());

    /**
     * Method utama yang dijalankan saat user menekan tombol ENTER di layar Login.
     * @param atm Context mesin ATM.
     * @param pin PIN yang diinputkan user.
     */
    @Override
    public void masukkanPin(MesinATM atm, String pin) {
        ProxyAkun proxy = (ProxyAkun) atm.getProxy(); 
        
        String nomorKartuTest = atm.getNomorKartuSementara(); 
        Akun akunTarget = atm.getBankDatabase().getAkun(nomorKartuTest);

        if (akunTarget == null) {
            JOptionPane.showMessageDialog(null, "Kartu tidak dikenali (Akun tidak ditemukan di Database)!", "Error Kartu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            proxy.login(akunTarget, pin);
            
            JOptionPane.showMessageDialog(null, "Login Berhasil! Selamat Datang.");
            
            atm.ubahState(new StateMenuUtama());  
            if (atm.getJendelaUtama() instanceof view.MainFrame mainframe) {
                mainframe.gantiLayar("MENU");
            } 
            LOGGER.info("[STATE] Transisi ke StateMenuUtama.");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Login Gagal: " + e.getMessage(), "Akses Ditolak", JOptionPane.WARNING_MESSAGE);
        }
    }

    // --- Method di bawah ini dikosongkan karena tidak relevan di layar Login ---

    @Override
    public void masukkanKartu(MesinATM atm, String nomorKartu) {
        // Tidak relevan, karena kartu dianggap sudah masuk
    }

    @Override 
    public void pilihMenu(MesinATM atm, int pilihan) {
        // User tidak bisa pilih menu saat belum login
    }

    @Override 
    public void prosesJumlah(MesinATM atm, double jumlah) {
        // User tidak bisa input nominal uang saat login
    }

    @Override 
    public void keluar(MesinATM atm) {
        // Logika tombol cancel/exit bisa ditaruh di sini jika ada
    }
}