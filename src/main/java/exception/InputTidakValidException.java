package exception;

/**
 * Kelas InputTidakValidException
 * Dilempar ketika input dari user tidak valid, misalnya nilai negatif,
 * format tidak sesuai, atau field kosong.
 */
public class InputTidakValidException extends Exception {

    // Constructor: menerima pesan error spesifik (misal: "Nominal tidak boleh negatif")
    public InputTidakValidException(String message) {
        super(message);
    }
}
