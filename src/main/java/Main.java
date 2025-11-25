import javax.swing.SwingUtilities;
import state.MesinATM;
import state.StateSiaga;
import view.MainFrame; // Menggunakan MainFrame

/**
 * Kelas Main
 * Titik masuk (Entry Point) aplikasi.
 * Bertugas menginisialisasi Mesin ATM, State awal, dan GUI.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            
            // 1. Hidupkan Mesin ATM
            MesinATM atm = new MesinATM();
            
            // 2. Set State Awal ke Siaga (Idle)
            atm.ubahState(new StateSiaga());

            // 3. Buat GUI (MainFrame) dan Hubungkan dengan Mesin
            MainFrame gui = new MainFrame(atm);
            atm.setJendelaUtama(gui);

            // 4. Tampilkan Layar Awal
            gui.gantiLayar("WELCOME");
            gui.setVisible(true);
        });
    }
}