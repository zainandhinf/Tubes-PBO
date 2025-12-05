package service;

import java.util.List;

import exception.SaldoKurangException;
import exception.GagalLoginException;
import exception.InputTidakValidException;
import exception.RekeningTidakDitemukanException;

/**
 * Interface ITransaksi
 * Merupakan "Kontrak Bisnis" yang mendefinisikan operasi apa saja 
 * yang harus dimiliki oleh sistem ATM (baik Proxy maupun RealAccount).
 */
public interface ITransaksi {

    /**
     * Melakukan penarikan uang.
     * @param jumlah Nominal uang yang ditarik.
     * @throws Exception Bisa berupa SaldoKurangException atau GagalLoginException.
     */
    void tarikTunai(double jumlah) 
        throws SaldoKurangException, GagalLoginException, InputTidakValidException;

    /**
     * Melakukan penyetoran uang tunai.
     * @param jumlah Nominal uang yang disetor.
     * @throws Exception Jika input negatif atau error koneksi.
     */
    void setorTunai(double jumlah) 
        throws InputTidakValidException, GagalLoginException;

    /**
     * Melakukan transfer saldo ke akun lain.
     * @param jumlah Nominal uang.
     * @param noRekTujuan Nomor rekening penerima.
     * @throws Exception Jika saldo kurang, tujuan tidak ada, atau belum login.
     */
    void transfer(double jumlah, String noRekTujuan) 
        throws SaldoKurangException, RekeningTidakDitemukanException, GagalLoginException, InputTidakValidException;

    /**
     * Mengembalikan informasi saldo terkini.
     * @return double jumlah saldo.
     */
    double cekSaldo();

    /**
     * Mengambil daftar riwayat transaksi.
     * @return List String berisi log transaksi.
     */
    List<String> getRiwayat();
}