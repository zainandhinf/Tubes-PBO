package model;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Kelas BankDatabase
 * Menerapkan Singleton Pattern & Java Collection Framework.
 * Bertindak sebagai pusat penyimpanan data nasabah (Single Source of Truth).
 * Mendukung mode In-Memory (Dummy) dan Database Eksternal (PostgreSQL).
 */
@SuppressWarnings("java:S6548")
public class BankDatabase {

    private static final Logger LOGGER = Logger.getLogger(BankDatabase.class.getName());
    
    // SINGLETON PATTERN: Variabel static untuk menyimpan satu-satunya instance
    private static BankDatabase instance;
    
    // JAVA COLLECTION FRAMEWORK: Penyimpanan utama menggunakan HashMap (Cache)
    // Key: String (Nomor Rekening), Value: Objek Akun
    private Map<String, Akun> accounts = new HashMap<>();

    private Properties config = new Properties();

    private String dbUrl;
    private String dbUser;
    private String dbPass;

    private static final String CONFIG_APP_MODE = "app.mode";
    private static final String MODE_MEMORY = "MEMORY";
    private static final String MODE_SQL = "SQL";

    // Constructor Private: Agar tidak bisa di-new sembarangan dari luar
    private BankDatabase() {
        loadConfig();

        String mode = config.getProperty(CONFIG_APP_MODE, MODE_SQL);
        
        if (mode.equalsIgnoreCase("SQL")) {
            LOGGER.info("[SYSTEM] Mode: DATABASE SQL (PostgreSQL)");
            loadDataFromSQL(); 
        } else {
            LOGGER.info("[SYSTEM] Mode: IN-MEMORY (Dummy Data)");
            loadDummyData();
        }        
    }

    // Method Static untuk mengambil instance (Singleton)
    public static BankDatabase getInstance() {
        if (instance == null) {
            instance = new BankDatabase();
        }
        return instance;
    }

    /**
     * Method untuk membaca file .properties dari folder resources
     */
    private void loadConfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                LOGGER.severe("[ERROR] File database.properties tidak ditemukan!");
                return;
            }
            config.load(input);
            this.dbUrl = config.getProperty("db.url");
            this.dbUser = config.getProperty("db.user");
            this.dbPass = config.getProperty("db.password");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------
    // KONEKSI & LOAD DATA (SQL)
    // ---------------------------------------------------------

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

    // ---------------------------------------------------------
    // KONEKSI KE POSTGRESQL
    // ---------------------------------------------------------
    private void loadDataFromSQL() {
        LOGGER.info(() -> "[DATABASE] Menghubungkan ke PostgreSQL...");

        try (Connection conn = getConnection()) {

            LOGGER.info(() -> "[DATABASE] Koneksi Sukses!");

            String sql = "SELECT no_rek, pin, saldo, tipe_akun FROM akun";

            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    String noRek = rs.getString("no_rek");
                    String pin = rs.getString("pin");
                    double saldo = rs.getDouble("saldo");
                    String tipe = rs.getString("tipe_akun");

                    Akun akunBaru = new AkunTabungan(noRek, pin, saldo, tipe);

                    // LOAD RIWAYAT TRANSAKSI UNTUK AKUN INI
                    loadHistoriTransaksi(conn, akunBaru);

                    tambahAkun(akunBaru);
                }

                LOGGER.info(() -> "[DATABASE] " + accounts.size() + 
                                " data nasabah & riwayat berhasil dimuat.");
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "[ERROR] Gagal konek ke Database!", e);
            loadDummyData(); // fallback
        }
    }


    /**
     * Mengambil data dari tabel 'transaksi' dan memasukkannya ke List di object Akun
     */
    private void loadHistoriTransaksi(Connection conn, Akun akun) {
        String sql = "SELECT jenis_transaksi, nominal, keterangan, waktu "
           + "FROM transaksi WHERE no_rek = ? "
           + "ORDER BY waktu DESC LIMIT 10";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, akun.getNoRek());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String jenis = rs.getString("jenis_transaksi");
                double nominal = rs.getDouble("nominal");
                String ket = rs.getString("keterangan");
                String waktu = rs.getString("waktu");
                
                String log = String.format("[%s] %s - Rp %,.0f (%s)", waktu, jenis, nominal, ket);
                
                akun.tambahHistoriInternal(log);
            }
        } catch (SQLException e) {
            LOGGER.warning(() -> "[WARN] Gagal load histori untuk " + akun.getNoRek());
        }
    }

    // ---------------------------------------------------------
    // METHOD UNTUK DATA DUMMY (JCF Only)
    // ---------------------------------------------------------
    private void loadDummyData() {
        LOGGER.info("[DATABASE] Loading data dummy ke memori...");
        
        tambahAkun(new AkunTabungan("123456", "123456", 5000000, "Platinum")); 
        tambahAkun(new AkunTabungan("654321", "111111", 50000, "Silver"));   
        tambahAkun(new AkunTabungan("999999", "000000", 0, "Gold"));
        
        LOGGER.info(() -> "[DATABASE] " + accounts.size() + " akun berhasil dimuat.");
    }

    // ---------------------------------------------------------
    // API / METHOD PUBLIK
    // ---------------------------------------------------------

    public Akun getAkun(String noRek) {
        return accounts.get(noRek);
    }

    public void tambahAkun(Akun akun) {
        if (akun != null) {
            accounts.put(akun.getNoRek(), akun);
        }
    }

    public boolean isAkunExist(String noRek) {
        return accounts.containsKey(noRek);
    }

    /**
     * Update saldo di memory dan (jika mode SQL) di database.
     */
    public void updateSaldo(Akun akun) {
        if (akun == null) return;
        // Update cache
        accounts.put(akun.getNoRek(), akun);

        String mode = config.getProperty(CONFIG_APP_MODE, MODE_MEMORY);
        if (mode.equalsIgnoreCase("SQL")) {
            String sql = "UPDATE akun SET saldo = ? WHERE no_rek = ?";
            try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDouble(1, akun.getSaldo());
                pstmt.setString(2, akun.getNoRek());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                LOGGER.warning(()-> "[WARN] Gagal update saldo ke DB untuk " + akun.getNoRek());
            }
        }
    }

    /**
     * Tambah catatan transaksi ke tabel transaksi (jika mode SQL) dan ke histori internal akun.
     */
    public void tambahTransaksi(String noRek, String jenis, double nominal, String keterangan) {
        Akun akun = accounts.get(noRek);
        String waktu = java.time.LocalDateTime.now().toString();
        String log = String.format("[%s] %s - Rp %,.0f (%s)", waktu, jenis, nominal, keterangan == null ? "" : keterangan);

        // Tambah ke histori internal jika akun ada di cache
        if (akun != null) {
            akun.tambahHistoriInternal(log);
        }

        String mode = config.getProperty(CONFIG_APP_MODE, MODE_MEMORY);
        if (mode.equalsIgnoreCase("SQL")) {
            String sql = "INSERT INTO transaksi (no_rek, jenis_transaksi, nominal, keterangan) VALUES (?, ?, ?, ?)";
            try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, noRek);
                pstmt.setString(2, jenis);
                pstmt.setDouble(3, nominal);
                pstmt.setString(4, keterangan);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, 
                    String.format("[WARN] Gagal insert transaksi ke DB untuk %s", noRek), 
                    e
                );
            }
        }
    }
}