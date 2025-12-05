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
 * Unit Test untuk fitur TARIK TUNAI
 * 
 * Test Coverage:
 * - Tarik tunai dengan nominal valid
 * - Tarik tunai nominal maksimal
 * - Tarik tunai dengan error handling (nominal invalid, saldo tidak cukup)
 * - Riwayat transaksi tercatat
 * - Integrasi dengan cek saldo
 */
@DisplayName("Tarik Tunai Test Suite")
class TarikTunaiTest {

    private Akun akunTest;
    private BankDatabase db;
    private ProxyAkun proxy;
    private AkunAsli akunAsli;

    @BeforeEach
    void setUp() {
        // Inisialisasi BankDatabase (Singleton)
        db = BankDatabase.getInstance();

        // Buat akun test dengan saldo awal Rp 1.000.000
        akunTest = new AkunTabungan("444444", "111111", 1_000_000, "Silver");
        db.tambahAkun(akunTest);

        // Inisialisasi proxy untuk testing
        proxy = new ProxyAkun();
        
        // Inisialisasi AkunAsli untuk operasi transaksi
        akunAsli = new AkunAsli(akunTest);
    }

    // ============================================================
    // TEST TARIK TUNAI
    // ============================================================

    @Test
    @DisplayName("Tarik tunai dengan nominal valid berhasil mengurangi saldo")
    void testTarikTunaiNominalValid() {
        double saldoAwal = akunTest.getSaldo();
        double nominalTarik = 250_000;

        assertDoesNotThrow(() -> akunAsli.tarikTunai(nominalTarik));
        
        double saldoAkhir = akunTest.getSaldo();
        assertEquals(saldoAwal - nominalTarik, saldoAkhir, 0.001,
            "Saldo harus berkurang sesuai nominal tarik");
    }

    @Test
    @DisplayName("Tarik tunai seluruh saldo berhasil")
    void testTarikTunaiSeluruhSaldo() {
        double saldoAwal = akunTest.getSaldo();

        assertDoesNotThrow(() -> akunAsli.tarikTunai(saldoAwal));
        
        assertEquals(0, akunTest.getSaldo(), 0.001, 
            "Saldo harus 0 setelah tarik seluruh saldo");
    }

    @Test
    @DisplayName("Tarik tunai melebihi saldo throw exception")
    void testTarikTunaiSaldoTidakCukup() {
        Exception exception = assertThrows(Exception.class, () -> {
            akunAsli.tarikTunai(2_000_000);
        });
        assertTrue(exception.getMessage().contains("Saldo tidak cukup"),
            "Pesan error harus berisi 'Saldo tidak cukup'");
    }

    @Test
    @DisplayName("Tarik tunai nominal 0 atau negatif throw exception")
    void testTarikTunaiNominalInvalid() {
        // Test nominal 0
        Exception exception1 = assertThrows(Exception.class, () -> {
            akunAsli.tarikTunai(0);
        });
        assertTrue(exception1.getMessage().contains("Nominal harus lebih dari 0"));
        
        // Test nominal negatif
        Exception exception2 = assertThrows(Exception.class, () -> {
            akunAsli.tarikTunai(-50_000);
        });
        assertTrue(exception2.getMessage().contains("Nominal harus lebih dari 0"));
    }

    @Test
    @DisplayName("Tarik tunai tercatat di riwayat transaksi dan konsisten dengan cek saldo")
    void testTarikTunaiRiwayatDanCekSaldo() throws GagalLoginException, Exception {
        proxy.login(akunTest, "111111");
        double saldoAwal = proxy.cekSaldo();
        
        akunAsli.tarikTunai(300_000);
        
        // Verifikasi saldo berkurang
        double saldoAkhir = proxy.cekSaldo();
        assertEquals(saldoAwal - 300_000, saldoAkhir, 0.001,
            "Saldo harus berkurang Rp 300.000");
        
        // Verifikasi riwayat tercatat
        var riwayat = akunTest.getHistoriTransaksi();
        assertTrue(riwayat.size() > 0, "Riwayat harus tidak kosong");
        
        String entryTerakhir = riwayat.get(0);
        assertTrue(entryTerakhir.contains("TARIK TUNAI") || entryTerakhir.contains("Tarik tunai Rp"),
            "Riwayat harus mencatat transaksi tarik tunai");
    }
}
