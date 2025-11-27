package service;

import java.util.Collections;
import java.util.List;

import exception.GagalLoginException;
import model.Akun;

/**
 * Kelas ProxyAkun
 * Menerapkan Proxy Pattern (Protection Proxy).
 * Bertindak sebagai lapisan keamanan (Security Layer) sebelum mengakses AkunAsli.
 * Memastikan user sudah login sebelum bisa melakukan transaksi.
 */
public class ProxyAkun implements ITransaksi {
    
    private AkunAsli akunAsli; // Referensi ke Objek Asli (Real Subject)
    private boolean isLogin = false; // Status Sesi

    /**
     * Method Khusus Proxy untuk melakukan Login.
     * Jika PIN benar, Proxy akan membuat instance AkunAsli.
     */
    public void login(Akun akun, String pinInput) throws GagalLoginException {
        if (akun != null && akun.getPin().equals(pinInput)) {
            this.isLogin = true;
            // Lazy Initialization: AkunAsli baru dibuat saat login sukses
            this.akunAsli = new AkunAsli(akun);
            System.out.println("[PROXY] Login Berhasil untuk: " + akun.getNoRek());
        } else {
            throw new GagalLoginException("PIN Salah! Akses ditolak.");
        }
    }

    /**
     * Method Khusus Proxy untuk Logout.
     * Menghancurkan sesi dan referensi ke AkunAsli.
     */
    public void logout() {
        this.isLogin = false;
        this.akunAsli = null;
        System.out.println("[PROXY] Sesi diakhiri.");
    }

    // --- IMPLEMENTASI ITRANSAKSI (Dicegat Dulu) ---

    @Override
    public void tarikTunai(double jumlah) throws Exception {
        if (isLogin) {
            akunAsli.tarikTunai(jumlah); // Teruskan ke Real Subject
        } else {
            throw new GagalLoginException("Akses Ditolak: Silakan login terlebih dahulu.");
        }
    }

    @Override
    public void setorTunai(double jumlah) throws Exception {
        if (isLogin) {
            akunAsli.setorTunai(jumlah);
        } else {
            throw new GagalLoginException("Akses Ditolak: Silakan login terlebih dahulu.");
        }
    }

    @Override
    public void transfer(double jumlah, String tujuan) throws Exception {
        if (isLogin) {
            akunAsli.transfer(jumlah, tujuan);
        } else {
            throw new GagalLoginException("Akses Ditolak: Silakan login terlebih dahulu.");
        }
    }

    @Override
    public double cekSaldo() {
        if (isLogin) {
            return akunAsli.cekSaldo();
        }
        return 0;
    }

    @Override
    public List<String> getRiwayat() {
        if (isLogin) {
            return akunAsli.getRiwayat();
        }
        return Collections.emptyList();
    }
}