package service;

import model.Akun;
import model.AkunTabungan;
import model.BankDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.NoSuchElementException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransferTest {

    private Akun akunPengirim;
    private Akun akunPenerima;
    private Akun akunLain;
    private AkunAsli serviceTransfer;
    private BankDatabase database;

    @BeforeEach
    void setUp() throws Exception {
        database = BankDatabase.getInstance();

        akunPengirim = new AkunTabungan("Budi Santoso", "12345", 1_000_000, "Gold");
        serviceTransfer = new AkunAsli(akunPengirim);

        akunPenerima = new AkunTabungan("Siti Aminah", "67890", 500_000, "Silver");
        akunLain     = new AkunTabungan("Ahmad Rahman", "11111", 200_000, "Bronze");

        mockDatabaseAccounts();
    }

    private void mockDatabaseAccounts() throws Exception {
        Field accountsField = BankDatabase.class.getDeclaredField("accounts");
        accountsField.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, Akun> accounts = (Map<String, Akun>) accountsField.get(database);

        accounts.clear();
        accounts.put(akunPengirim.getNoRek(), akunPengirim);
        accounts.put(akunPenerima.getNoRek(), akunPenerima);
        accounts.put(akunLain.getNoRek(), akunLain);
    }

    // 1. TRANSFER BERHASIL
    @Test
    @DisplayName("1. Transfer nominal valid & saldo cukup - Berhasil")
    void testTransferBerhasil() {
        serviceTransfer.transfer(300_000, akunPenerima.getNoRek());

        assertEquals(700_000, akunPengirim.getSaldo());
        assertEquals(800_000, akunPenerima.getSaldo());
    }

    // 2. SALDO TIDAK CUKUP
    @Test
    @DisplayName("2. Transfer saldo kurang - Harus gagal")
    void testTransferSaldoTidakCukup() {
        String noRekPenerima = akunPenerima.getNoRek();

        Exception ex = assertThrows(
            IllegalStateException.class,
            () -> serviceTransfer.transfer(2_000_000, noRekPenerima)
        );

        assertEquals("Saldo tidak cukup", ex.getMessage());
        assertEquals(1_000_000, akunPengirim.getSaldo());
    }

    // 3. REKENING TUJUAN TIDAK ADA
    @Test
    @DisplayName("3. Transfer ke rekening yang tidak ada - Gagal")
    void testTransferRekeningTidakAda() {
        Exception ex = assertThrows(
            NoSuchElementException.class,
            () -> serviceTransfer.transfer(100_000, "99999")
        );

        assertEquals("Rekening tujuan tidak ditemukan", ex.getMessage());
    }

    // 4. TRANSFER KE REKENING SENDIRI
    @Test
    @DisplayName("4. Transfer ke rekening sendiri - Gagal")
    void testTransferKeRekeningSendiri() {
        String noRekSendiri = akunPengirim.getNoRek();

        Exception ex = assertThrows(
            IllegalArgumentException.class,
            () -> serviceTransfer.transfer(100_000, noRekSendiri)
        );

        assertEquals("Tidak bisa transfer ke rekening sendiri", ex.getMessage());
    }

    // 5. MULTIPLE TRANSFER
    @Test
    @DisplayName("5. Multiple transfer - Saldo & histori konsisten")
    void testMultipleTransfer() {
        serviceTransfer.transfer(200_000, akunPenerima.getNoRek());
        serviceTransfer.transfer(150_000, akunLain.getNoRek());

        // Verifikasi saldo
        assertEquals(650_000, akunPengirim.getSaldo());
        assertEquals(700_000, akunPenerima.getSaldo());
        assertEquals(350_000, akunLain.getSaldo());

        // Verifikasi histori
        List<String> histori = akunPengirim.getHistoriTransaksi();
        long count = histori.stream().filter(h -> h.contains("Transfer ke")).count();
        assertEquals(2, count);
    }
}
