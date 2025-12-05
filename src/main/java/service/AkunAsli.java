package service;

import java.util.List;
import java.util.NoSuchElementException;

import model.Akun;
import model.BankDatabase;

/**
 * Kelas AkunAsli (Real Subject)
 * Merupakan implementasi "Real Subject" dalam Proxy Pattern.
 * Kelas ini berisi logika bisnis sesungguhnya (pengurangan/penambahan saldo).
 */
public class AkunAsli implements ITransaksi {
    private Akun akun;

    // Constructor: Menerima data akun yang sudah lolos validasi Proxy
    public AkunAsli(Akun akun) {
        this.akun = akun;
    }

    @Override
    public void tarikTunai(double jumlah) {
        if (jumlah <= 0) throw new IllegalArgumentException("Nominal harus lebih dari 0");
        if (akun.getSaldo() < jumlah) throw new IllegalStateException("Saldo tidak cukup");

        double saldoLama = akun.getSaldo();
        akun.setSaldo(saldoLama - jumlah);
        akun.tambahHistoriInternal(String.format("Tarik tunai Rp %,.0f", jumlah));

        // Persist perubahan ke BankDatabase
        BankDatabase db = BankDatabase.getInstance();
        db.updateSaldo(akun);
        db.tambahTransaksi(akun.getNoRek(), "TARIK TUNAI", jumlah, "ATM");
    }

    @Override
    public void setorTunai(double jumlah) {
        if (jumlah <= 0) throw new IllegalArgumentException("Nominal harus lebih dari 0");
        double saldoLama = akun.getSaldo();
        akun.setSaldo(saldoLama + jumlah);
        akun.tambahHistoriInternal(String.format("Setor tunai Rp %,.0f", jumlah));
        // Persist perubahan
        BankDatabase db = BankDatabase.getInstance();
        db.updateSaldo(akun);
        db.tambahTransaksi(akun.getNoRek(), "SETOR TUNAI", jumlah, "ATM");
    }

    @Override
    public void transfer(double jumlah, String tujuan) {
        if (jumlah <= 0) throw new IllegalArgumentException("Nominal harus lebih dari 0");
        if (akun.getSaldo() < jumlah) throw new IllegalStateException("Saldo tidak cukup");
        if (akun.getNoRek().equals(tujuan)) throw new IllegalArgumentException("Tidak bisa transfer ke rekening sendiri");
        BankDatabase db = BankDatabase.getInstance();
        Akun akunTujuan = db.getAkun(tujuan);
        if (akunTujuan == null) throw new NoSuchElementException("Rekening tujuan tidak ditemukan");
        akun.setSaldo(akun.getSaldo() - jumlah);
        akunTujuan.setSaldo(akunTujuan.getSaldo() + jumlah);
        akun.tambahHistoriInternal(String.format("Transfer ke %s Rp %,.0f", tujuan, jumlah));
        akunTujuan.tambahHistoriInternal(String.format("Transfer dari %s Rp %,.0f", akun.getNoRek(), jumlah));
        // Persist perubahan untuk kedua akun dan catat transaksi
        db.updateSaldo(akun);
        db.updateSaldo(akunTujuan);
        db.tambahTransaksi(akun.getNoRek(), "TRANSFER KELUAR", jumlah, "Ke " + tujuan);
        db.tambahTransaksi(akunTujuan.getNoRek(), "TRANSFER MASUK", jumlah, "Dari " + akun.getNoRek());
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