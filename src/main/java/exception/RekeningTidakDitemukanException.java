package exception;

/**
 * Kelas RekeningTidakDitemukanException
 * Dilempar ketika nomor rekening tujuan tidak ditemukan saat melakukan transfer.
 */
public class RekeningTidakDitemukanException extends Exception {

    // Constructor: menerima pesan error spesifik (misal: "Rekening tujuan tidak ditemukan")
    public RekeningTidakDitemukanException(String message) {
        super(message);
    }
}
