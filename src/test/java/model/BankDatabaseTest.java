package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankDatabaseTest {

    private BankDatabase db;

    @BeforeEach
    void setUp() {
        // Reset singleton (hanya simulasi, karena singleton sulit di-reset total tanpa refleksi)
        // Kita cukup ambil instance yang ada
        db = BankDatabase.getInstance();
    }

    @Test
    void testSingletonInstance() {
        BankDatabase db2 = BankDatabase.getInstance();
        assertSame(db, db2, "BankDatabase harus berupa Singleton (Instance sama)");
    }

    @Test
    void testGetAkunValid() {
        // Akun dummy 123456 harusnya selalu ada (dari loadDummyData)
        // Asumsi mode MEMORY aktif saat testing
        Akun akun = db.getAkun("123456");
        assertNotNull(akun, "Akun 123456 harus ditemukan");
        assertEquals("123456", akun.getPin(), "PIN harus sesuai dummy data");
    }

    @Test
    void testGetAkunInvalid() {
        Akun akun = db.getAkun("000000"); // Akun tidak ada
        assertNull(akun, "Akun tidak terdaftar harus mengembalikan null");
    }
}