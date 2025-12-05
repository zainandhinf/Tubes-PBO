package exception;

/**
 * Kelas GagalLoginException
 * Custom Exception yang dilempar (thrown) ketika proses autentikasi gagal.
 * Digunakan khusus oleh ProxyAkun saat validasi PIN tidak cocok atau user mencoba
 * mengakses fitur tanpa login.
 */
public class GagalLoginException extends RuntimeException  {
    
    // Constructor: Menerima pesan error spesifik (misal: "PIN Salah" atau "Belum Login")
    public GagalLoginException(String message) {
        super(message);
    }
}