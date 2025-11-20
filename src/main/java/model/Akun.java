package model;

public abstract class Akun {
    private String noRek;
    private String pin;
    private double saldo;

    // Constructor
    public Akun(String noRek, String pin, double saldo) {
        this.noRek = noRek;
        this.pin = pin;
        this.saldo = saldo;
    }

    // Getters and Setters
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

    public abstract void infoAkun();
}
