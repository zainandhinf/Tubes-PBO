package service;

import model.Akun;
import model.AkunTabungan;
import model.BankDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AkunAsliTest {

    private Akun akunSaya;
    private AkunAsli service;

    @BeforeEach
    void setUp() {
        // Setup akun sendiri
        akunSaya = new AkunTabungan("Me", "111", 500000, "Gold");
        service = new AkunAsli(akunSaya);
        
        // Setup akun tujuan di Database (Inject manual ke map)
        Akun akunTeman = new AkunTabungan("Friend", "222", 0, "Silver");
        BankDatabase.getInstance().tambahAkun(akunTeman); // Pastikan method ini publik di BankDatabase
    }

    @Test
    void testTarikTunaiBerhasil() {
        assertDoesNotThrow(() -> service.tarikTunai(100000));
        assertEquals(400000, akunSaya.getSaldo(), "Saldo harus berkurang 100k");
    }

    @Test
    void testTarikTunaiSaldoKurang() {
        Exception e = assertThrows(IllegalStateException.class, () -> {
            service.tarikTunai(600000); // Lebih dari saldo
        });
        assertEquals("Saldo tidak cukup", e.getMessage());
    }

    @Test
    void testSetorTunai() {
        assertDoesNotThrow(() -> service.setorTunai(50000));
        assertEquals(550000, akunSaya.getSaldo(), "Saldo harus bertambah");
    }

    @Test
    void testTransferBerhasil() {
        assertDoesNotThrow(() -> service.transfer(100000, "Friend"));
        
        assertEquals(400000, akunSaya.getSaldo(), "Saldo pengirim berkurang");
        
        // Cek saldo penerima via Database
        Akun penerima = BankDatabase.getInstance().getAkun("Friend");
        assertEquals(100000, penerima.getSaldo(), "Saldo penerima bertambah");
    }

    @Test
    void testTransferKeRekeningTidakAda() {
        Exception e = assertThrows(java.util.NoSuchElementException.class, () -> {
            service.transfer(50000, "Hantu");
        });
        assertEquals("Rekening tujuan tidak ditemukan", e.getMessage());
    }
}