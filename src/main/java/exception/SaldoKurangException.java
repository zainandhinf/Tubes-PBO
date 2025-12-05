package exception;

/**
 * Kelas SaldoKurangException
 * Dilempar ketika saldo rekening tidak mencukupi untuk melakukan transaksi,
 * seperti penarikan tunai atau transfer.
 */
public class SaldoKurangException extends Exception {

    // Constructor: menerima pesan error spesifik (misal: "Saldo tidak cukup")
    public SaldoKurangException(String message) {
        super(message);
    }
}
