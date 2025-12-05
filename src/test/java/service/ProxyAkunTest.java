package service;

import exception.GagalLoginException;
import model.Akun;
import model.AkunTabungan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProxyAkunTest {

    private ProxyAkun proxy;
    private Akun akunTest;

    @BeforeEach
    void setUp() {
        proxy = new ProxyAkun();
        // Buat akun dummy untuk tes (tidak perlu ambil dari DB)
        akunTest = new AkunTabungan("TestUser", "123456", 1000000, "Silver");
    }

    @Test
    void testLoginSukses() {
        assertDoesNotThrow(() -> proxy.login(akunTest, "123456"));
        // Setelah login, cekSaldo harus bisa diakses
        assertEquals(1000000, proxy.cekSaldo());
    }

    @Test
    void testLoginGagalPinSalah() {
        Exception exception = assertThrows(GagalLoginException.class, () -> {
            proxy.login(akunTest, "999999"); // PIN Salah
        });
        assertEquals("PIN Salah! Akses ditolak.", exception.getMessage());
    }

    @Test
    void testAksesTanpaLogin() {
        // Belum login, coba tarik tunai
        Exception exception = assertThrows(GagalLoginException.class, () -> {
            proxy.tarikTunai(50000);
        });
        assertTrue(exception.getMessage().contains("Silakan login"));
    }

    @Test
    void testLogout() throws GagalLoginException {
        // Login dulu
        proxy.login(akunTest, "123456");
        
        // Logout dari sistem
        proxy.logout();
        
        // Verifikasi cek saldo setelah logout (Return 0)
        double saldo = proxy.cekSaldo();
        assertEquals(0.0, saldo, "Saldo harus 0 saat tidak login");
        
        // Coba transaksi setelah logout (Harus Exception)
        assertThrows(GagalLoginException.class, () -> {
            proxy.tarikTunai(50000);
        });
    }
}