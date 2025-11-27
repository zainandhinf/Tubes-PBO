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

/**
 * Kelas BankDatabase
 * Menerapkan Singleton Pattern & Java Collection Framework.
 * Bertindak sebagai pusat penyimpanan data nasabah (Single Source of Truth).
 * Mendukung mode In-Memory (Dummy) dan Database Eksternal (PostgreSQL).
 */
public class BankDatabase {
    
    // 1. SINGLETON PATTERN: Variabel static untuk menyimpan satu-satunya instance
    private static BankDatabase instance;
    
    // 2. JAVA COLLECTION FRAMEWORK: Penyimpanan utama menggunakan HashMap (Cache)
    // Key: String (Nomor Rekening), Value: Objek Akun
    private Map<String, Akun> accounts = new HashMap<>();

    private Properties config = new Properties();

    // Simpan kredensial di variabel agar mudah diakses
    private String dbUrl;
    private String dbUser;
    private String dbPass;

    // Constructor Private: Agar tidak bisa di-new sembarangan dari luar
    private BankDatabase() {
        loadConfig();

        String mode = config.getProperty("app.mode", "MEMORY");
        
        if (mode.equalsIgnoreCase("SQL")) {
            System.out.println("[SYSTEM] Mode: DATABASE SQL (PostgreSQL)");
            loadDataFromSQL(); 
        } else {
            System.out.println("[SYSTEM] Mode: IN-MEMORY (Dummy Data)");
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
                System.out.println("[ERROR] File database.properties tidak ditemukan!");
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
        System.out.println("[DATABASE] Menghubungkan ke PostgreSQL...");

        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("[DATABASE] Koneksi Sukses!");
                
                String sql = "SELECT * FROM akun";
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
                    System.out.println("[DATABASE] " + accounts.size() + " data nasabah & riwayat berhasil dimuat.");
                }
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] Gagal konek ke Database: " + e.getMessage());
            loadDummyData(); // Fallback
        }
    }

    /**
     * Mengambil data dari tabel 'transaksi' dan memasukkannya ke List di object Akun
     */
    private void loadHistoriTransaksi(Connection conn, Akun akun) {
        String sql = "SELECT * FROM transaksi WHERE no_rek = ? ORDER BY waktu DESC LIMIT 10";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, akun.getNoRek());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String jenis = rs.getString("jenis_transaksi");
                double nominal = rs.getDouble("nominal");
                String ket = rs.getString("keterangan");
                String waktu = rs.getString("waktu"); // Timestamp jadi String
                
                // Format: [2023-11-20 10:00] TARIK TUNAI - Rp 50.000 (ATM Kampus)
                String log = String.format("[%s] %s - Rp %,.0f (%s)", waktu, jenis, nominal, ket);
                
                // Masukkan ke List di Memory (Tanpa simpan balik ke DB)
                akun.tambahHistoriInternal(log);
            }
        } catch (SQLException e) {
            System.out.println("[WARN] Gagal load histori untuk " + akun.getNoRek());
        }
    }

    // ---------------------------------------------------------
    // METHOD UNTUK DATA DUMMY (JCF Only)
    // ---------------------------------------------------------
    private void loadDummyData() {
        System.out.println("[DATABASE] Loading data dummy ke memori...");
        
        tambahAkun(new AkunTabungan("123456", "123456", 5000000, "Platinum")); 
        tambahAkun(new AkunTabungan("654321", "111111", 50000, "Silver"));   
        tambahAkun(new AkunTabungan("999999", "000000", 0, "Gold"));
        
        System.out.println("[DATABASE] " + accounts.size() + " akun berhasil dimuat.");
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
}