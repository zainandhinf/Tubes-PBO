package service;

import java.util.List;

import model.Akun;
import model.BankDatabase;

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
        if (jumlah <= 0) throw new Exception("Nominal harus lebih dari 0");
        double saldoLama = akun.getSaldo();
        akun.setSaldo(saldoLama + jumlah);
        akun.tambahHistoriInternal(String.format("Setor tunai Rp %,.0f", jumlah));
    }

    @Override
    public void transfer(double jumlah, String tujuan) throws Exception {
        if (jumlah <= 0) throw new Exception("Nominal harus lebih dari 0");
        if (akun.getSaldo() < jumlah) throw new Exception("Saldo tidak cukup");
        BankDatabase db = BankDatabase.getInstance();
        Akun akunTujuan = db.getAkun(tujuan);
        if (akunTujuan == null) throw new Exception("Rekening tujuan tidak ditemukan");
        akun.setSaldo(akun.getSaldo() - jumlah);
        akunTujuan.setSaldo(akunTujuan.getSaldo() + jumlah);
        akun.tambahHistoriInternal(String.format("Transfer ke %s Rp %,.0f", tujuan, jumlah));
        akunTujuan.tambahHistoriInternal(String.format("Transfer dari %s Rp %,.0f", akun.getNoRek(), jumlah));
    }

    @Override
    public double cekSaldo() {
        // Mengembalikan saldo asli dari objek Akun
        return akun.getSaldo();
    }

    @Override
    public List<String> getRiwayat() {
        return akun.getHistoriTransaksi();
    }
}