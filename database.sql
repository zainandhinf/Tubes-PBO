-- 1. Buat Database
CREATE DATABASE db_atm_simulasi;

-- 2. Gunakan Database
USE db_atm_simulasi;

-- 3. Buat Tabel Akun
CREATE TABLE akun (
    no_rek VARCHAR(20) PRIMARY KEY,
    pin VARCHAR(6) NOT NULL,
    saldo DOUBLE DEFAULT 0,
    tipe_akun VARCHAR(20) DEFAULT 'Silver'
);

-- 4. Isi Data Dummy (Seeding)
INSERT INTO akun (no_rek, pin, saldo, tipe_akun) VALUES 
('123456', '123456', 5000000, 'Silver'),
('654321', '111111', 100000, 'Gold');