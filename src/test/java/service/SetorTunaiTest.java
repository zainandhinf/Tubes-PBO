package service;

import model.Akun;
import model.AkunTabungan;
import model.BankDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class SetorTunaiTest {

    private Akun akunTesting;
    private AkunAsli serviceSetor;
    private final double SALDO_AWAL = 250000;

    @BeforeEach
    void setUp() {
        akunTesting = new AkunTabungan("123341", "54321", SALDO_AWAL, "Regular");
        serviceSetor = new AkunAsli(akunTesting);
    }

    @Test
    @DisplayName("1. Setor tunai nominal valid - Harus berhasil")
    void testSetorTunaiBerhasil() {
        double nominal = 100000;
        assertDoesNotThrow(() -> serviceSetor.setorTunai(nominal));
        assertEquals(SALDO_AWAL + nominal, akunTesting.getSaldo());
    }

    @Test
    @DisplayName("2. Setor tunai nominal negatif - Harus gagal")
    void testSetorTunaiNominalNegatif() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> serviceSetor.setorTunai(-50000));
        assertEquals("Nominal harus lebih dari 0", ex.getMessage());
        assertEquals(SALDO_AWAL, akunTesting.getSaldo());
    }

    @Test
    @DisplayName("3. Setor tunai nominal nol - Harus gagal")
    void testSetorTunaiNominalNol() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> serviceSetor.setorTunai(0));
        assertEquals("Nominal harus lebih dari 0", ex.getMessage());
        assertEquals(SALDO_AWAL, akunTesting.getSaldo());
    }

    @Test
    @DisplayName("4. Multiple setor tunai - Saldo harus konsisten")
    void testMultipleSetorTunai() {
        serviceSetor.setorTunai(100000);
        serviceSetor.setorTunai(50000);
        assertEquals(SALDO_AWAL + 150000, akunTesting.getSaldo());
    }

    @Test
    @DisplayName("5. Setor tunai nominal minimum valid (Rp 1) - Harus berhasil")
    void testSetorTunaiNominalMinimum() {
        serviceSetor.setorTunai(1);
        assertEquals(SALDO_AWAL + 1, akunTesting.getSaldo());
    }
}
