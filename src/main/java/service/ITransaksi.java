package service;

/**
 * Interface ITransaksi
 * Merupakan "Kontrak Bisnis" yang mendefinisikan operasi apa saja 
 * yang harus dimiliki oleh sistem ATM (baik Proxy maupun RealAccount).
 * * Penanggung Jawab: Bersama (Disepakati di awal)
 */
public interface ITransaksi {

    /**
     * Melakukan penarikan uang.
     * @param jumlah Nominal uang yang ditarik.
     * @throws Exception Bisa berupa SaldoKurangException atau GagalLoginException.
     */
    void tarikTunai(double jumlah) throws Exception;

    /**
     * Melakukan penyetoran uang tunai.
     * @param jumlah Nominal uang yang disetor.
     * @throws Exception Jika input negatif atau error koneksi.
     */
    void setorTunai(double jumlah) throws Exception;

    /**
     * Melakukan transfer saldo ke akun lain.
     * @param jumlah Nominal uang.
     * @param noRekTujuan Nomor rekening penerima.
     * @throws Exception Jika saldo kurang, tujuan tidak ada, atau belum login.
     */
    void transfer(double jumlah, String noRekTujuan) throws Exception;

    /**
     * Mengembalikan informasi saldo terkini.
     * @return double jumlah saldo.
     */
    double cekSaldo();
}