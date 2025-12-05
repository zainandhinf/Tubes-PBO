package service;

import exception.GagalLoginException;
import model.Akun;
import model.AkunTabungan;
import model.BankDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test untuk fitur CEK SALDO
 * 
 * Test Coverage:
 * - Cek saldo sebelum login
 * - Cek saldo setelah login
 * - Cek saldo setelah tarik tunai
 * - Cek saldo setelah setor tunai
 * - Cek saldo setelah logout
 */
@DisplayName("Cek Saldo Test Suite")
class CekSaldoTest {

    private Akun akunTest;
    private BankDatabase db;
    private ProxyAkun proxy;
    private AkunAsli akunAsli;

    @BeforeEach
    void setUp() {
        // Inisialisasi BankDatabase (Singleton)
        db = BankDatabase.getInstance();

        // Buat akun test dengan saldo awal Rp 1.000.000
        akunTest = new AkunTabungan("333333", "111111", 1_000_000, "Platinum");
        db.tambahAkun(akunTest);

        // Inisialisasi proxy untuk testing
        proxy = new ProxyAkun();
        
        // Inisialisasi AkunAsli untuk operasi transaksi
        akunAsli = new AkunAsli(akunTest);
    }

    // ============================================================
    // TEST CEK SALDO
    // ============================================================

    @Test
    @DisplayName("Cek saldo sebelum login harus return 0")
    void testCekSaldoSebelumLogin() {
        double saldo = proxy.cekSaldo();
        assertEquals(0, saldo, "Cek saldo sebelum login harus return 0");
    }

    @Test
    @DisplayName("Cek saldo setelah login menampilkan saldo yang benar")
    void testCekSaldoSetelahLogin() throws GagalLoginException {
        proxy.login(akunTest, "111111");
        double saldo = proxy.cekSaldo();
        assertEquals(1_000_000, saldo, 0.001, "Saldo harus 1.000.000 setelah login");
    }

    @Test
    @DisplayName("Cek saldo setelah tarik tunai menunjukkan pengurangan saldo")
    void testCekSaldoSetelahTarikTunai() throws GagalLoginException {
        proxy.login(akunTest, "111111");
        akunAsli.tarikTunai(250_000);
        
        double saldo = proxy.cekSaldo();
        assertEquals(750_000, saldo, 0.001, 
            "Saldo seharusnya berkurang menjadi Rp 750.000");
    }

    @Test
    @DisplayName("Cek saldo setelah setor tunai menunjukkan penambahan saldo")
    void testCekSaldoSetelahSetorTunai() throws GagalLoginException {
        proxy.login(akunTest, "111111");
        akunAsli.setorTunai(500_000);
        
        double saldo = proxy.cekSaldo();
        assertEquals(1_500_000, saldo, 0.001,
            "Saldo seharusnya bertambah menjadi Rp 1.500.000");
    }

    @Test
    @DisplayName("Cek saldo setelah logout return 0")
    void testCekSaldoSetelahLogout() throws GagalLoginException {
        proxy.login(akunTest, "111111");
        assertEquals(1_000_000, proxy.cekSaldo());
        
        proxy.logout();
        
        double saldo = proxy.cekSaldo();
        assertEquals(0, saldo, "Saldo harus 0 setelah logout");
    }
}
