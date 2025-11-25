package service;

import model.Akun;

/**
 * Kelas AkunAsli (Real Subject)
 * Merupakan implementasi "Real Subject" dalam Proxy Pattern.
 * Kelas ini berisi logika bisnis sesungguhnya (pengurangan/penambahan saldo).
 * * ⚠️ CATATAN PENTING:
 * File ini adalah kerangka sementara (Dummy) agar ProxyAkun tidak error saat di-compile.
 * Logika detail (matematika saldo) akan diisi oleh PIC: Faridha & Suci.
 */
public class AkunAsli implements ITransaksi {
    private Akun akun;

    // Constructor: Menerima data akun yang sudah lolos validasi Proxy
    public AkunAsli(Akun akun) {
        this.akun = akun;
    }

    @Override
    public void tarikTunai(double jumlah) throws Exception {
        // TODO: (Tugas Faridha) Implementasi logika pengurangan saldo di sini
        // Contoh: if (saldo < jumlah) throw new SaldoKurangException...
    }

    @Override
    public void setorTunai(double jumlah) throws Exception {
        // TODO: (Tugas Suci) Implementasi logika penambahan saldo di sini
    }

    @Override
    public void transfer(double jumlah, String tujuan) throws Exception {
        // TODO: (Tugas Suci) Implementasi logika transfer antar akun di sini
    }

    @Override
    public double cekSaldo() {
        // Mengembalikan saldo asli dari objek Akun
        return akun.getSaldo();
    }
}