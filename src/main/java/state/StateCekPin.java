package state;

import javax.swing.JOptionPane;
import model.Akun;
import service.ProxyAkun;

/**
 * Kelas StateCekPin (Concrete State)
 * Merepresentasikan kondisi mesin ATM saat sedang menunggu input PIN dari user.
 * Bertugas menghubungkan TampilanLogin (View) dengan ProxyAkun (Security).
 */
public class StateCekPin implements StateATM {

    /**
     * Method utama yang dijalankan saat user menekan tombol ENTER di layar Login.
     * @param atm Context mesin ATM.
     * @param pin PIN yang diinputkan user.
     */
    @Override
    public void masukkanPin(MesinATM atm, String pin) {
        // 1. Ambil Proxy dari ATM (Sekarang Proxy ada di ATM/Context)
        // Kita melakukan casting karena atm.getProxy() mengembalikan tipe ITransaksi
        ProxyAkun proxy = (ProxyAkun) atm.getProxy(); 
        
        // 2. Ambil Database untuk mencari akun (Simulasi kartu dimasukkan)
        // Dalam simulasi ini, kita menganggap kartu "123456" yang dimasukkan secara otomatis.
        String nomorKartuTest = atm.getNomorKartuSementara(); 
        Akun akunTarget = atm.getBankDatabase().getAkun(nomorKartuTest);

        // Validasi awal: Cek apakah nomor rekening ada di Database
        if (akunTarget == null) {
            JOptionPane.showMessageDialog(null, "Kartu tidak dikenali (Akun tidak ditemukan di Database)!", "Error Kartu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 3. Coba Login ke Proxy (Security Layer)
            // Jika PIN salah, method ini akan melempar GagalLoginException
            proxy.login(akunTarget, pin);
            
            // 4. Jika Sukses (Tidak ada Exception):
            JOptionPane.showMessageDialog(null, "Login Berhasil! Selamat Datang.");
            
            atm.ubahState(new StateMenuUtama());  // Pindah Logic
            if (atm.getJendelaUtama() instanceof view.MainFrame) {
                ((view.MainFrame) atm.getJendelaUtama()).gantiLayar("MENU"); // Pindah GUI
            } 
            System.out.println("[STATE] Transisi ke StateMenuUtama..."); 
            
        } catch (Exception e) {
            // Tangkap error dari Proxy (PIN Salah)
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