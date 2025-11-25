package state;

import javax.swing.JFrame;
import model.BankDatabase;
import service.ITransaksi;
import service.ProxyAkun;

/**
 * Kelas MesinATM (Context)
 * Menyimpan status (State) saat ini dan data sesi user.
 */
public class MesinATM {
    
    private StateATM stateSaatIni;
    private BankDatabase bankDatabase;
    
    // Menggunakan Interface ITransaksi tapi isinya adalah ProxyAkun
    private ITransaksi proxy; 
    
    // Referensi ke GUI Utama agar State bisa ganti-ganti layar
    private JFrame jendelaUtama; 

    public MesinATM() {
        this.bankDatabase = BankDatabase.getInstance();
        // State awal adalah Siaga (Menunggu Kartu)
        // Pastikan Anda nanti membuat StateSiaga.java juga!
        // Untuk sementara jika StateSiaga belum ada, bisa di-null dulu atau buat dummy.
        // this.stateSaatIni = new StateSiaga(); 
        
        this.proxy = new ProxyAkun();
    }

    // --- Method Delegasi ke State Saat Ini ---

    public void masukkanKartu(String nomorKartu) {
        stateSaatIni.masukkanKartu(this, nomorKartu);
    }

    public void masukkanPin(String pin) {
        stateSaatIni.masukkanPin(this, pin);
    }

    public void pilihMenu(int pilihan) {
        stateSaatIni.pilihMenu(this, pilihan);
    }

    public void prosesJumlah(double jumlah) {
        stateSaatIni.prosesJumlah(this, jumlah);
    }

    public void keluar() {
        stateSaatIni.keluar(this);
    }

    // --- Getter & Setter ---

    public void ubahState(StateATM stateBaru) {
        this.stateSaatIni = stateBaru;
    }

    public StateATM getStateSaatIni() {
        return stateSaatIni;
    }

    public BankDatabase getBankDatabase() {
        return bankDatabase;
    }

    public ITransaksi getProxy() {
        return proxy;
    }

    public JFrame getJendelaUtama() {
        return jendelaUtama;
    }

    public void setJendelaUtama(JFrame frame) {
        this.jendelaUtama = frame;
    }
}