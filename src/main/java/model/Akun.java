package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Class Akun
 * Menerapkan konsep Encapsulation (private fields) dan Abstraction.
 * Berfungsi sebagai template dasar data nasabah dan menyimpan riwayat transaksi.
 */
public abstract class Akun {
    private String noRek;
    private String pin;
    private double saldo;
    private String tipeAkun;
    
    // JAVA COLLECTION FRAMEWORK: List untuk menyimpan histori transaksi
    private List<String> historiTransaksi = new ArrayList<>();

    // Constructor
    public Akun(String noRek, String pin, double saldo, String tipeAkun) {
        this.noRek = noRek;
        this.pin = pin;
        this.saldo = saldo;
        this.tipeAkun = tipeAkun;
    }

    // --- Method Tambahan untuk Fitur Riwayat ---
    
    public void tambahHistori(String pesan) {
        this.historiTransaksi.add(pesan);
    }

    public List<String> getHistoriTransaksi() {
        return historiTransaksi;
    }

    // --- Getters and Setters (Encapsulation) ---

    public String getNoRek() {
        return noRek;
    }

    public void setNoRek(String noRek) {
        this.noRek = noRek;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getTipeAkun() {
        return tipeAkun;
    }

    public void setTipeAkun(String tipeAkun) {
        this.tipeAkun = tipeAkun;
    }

    // Abstract method untuk Polymorphism
    public abstract void infoAkun();
}