-- 1. Hapus Database Lama (Jika Ada)
DROP DATABASE IF EXISTS db_atm_simulasi;

-- 2. Buat Database Baru
CREATE DATABASE db_atm_simulasi;

-- 3. Gunakan Database
\c db_atm_simulasi;

-- 4. Buat Tabel AKUN (Data Nasabah)
CREATE TABLE akun (
    no_rek VARCHAR(20) PRIMARY KEY,
    pin VARCHAR(6) NOT NULL,
    saldo DOUBLE PRECISION DEFAULT 0,
    tipe_akun VARCHAR(20) DEFAULT 'Silver'
);

-- 5. Buat Tabel TRANSAKSI (Untuk Fitur Riwayat/Mutasi)
CREATE TABLE transaksi (
    id SERIAL PRIMARY KEY, -- Pengganti AUTO_INCREMENT
    no_rek VARCHAR(20),
    jenis_transaksi VARCHAR(50), -- Contoh: "Tarik Tunai", "Setor", "Transfer Keluar"
    nominal DOUBLE PRECISION,
    keterangan VARCHAR(255), -- Contoh: "Ke 9999", "Admin Fee"
    waktu TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (no_rek) REFERENCES akun(no_rek) ON DELETE CASCADE
);

-- 6. Isi Data Dummy Akun (Seeding)
INSERT INTO akun (no_rek, pin, saldo, tipe_akun) VALUES 
('123456', '123456', 5000000, 'Silver'),
('654321', '111111', 100000, 'Gold'),
('999999', '000000', 0, 'Platinum');

-- 7. Isi Data Dummy Riwayat (Opsional)
INSERT INTO transaksi (no_rek, jenis_transaksi, nominal, keterangan) VALUES
('123456', 'SETOR TUNAI', 5000000, 'Setoran Awal'),
('123456', 'TARIK TUNAI', 50000, 'ATM Kampus'),
('654321', 'SETOR TUNAI', 100000, 'Setoran Awal');
