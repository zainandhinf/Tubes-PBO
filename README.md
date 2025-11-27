# 🏧 Simulasi Mesin ATM

> **Tugas Besar Pemrograman Berorientasi Objek (PBO)**
> Aplikasi simulasi ATM dengan antarmuka GUI (Split Layout) yang menerapkan Design Pattern, Database Integration, dan Clean Architecture.

---

## 📋 Fitur
1.  **GUI Realistis:** Menggunakan konsep **Split Layout**. Panel Kiri sebagai Layar Monitor (Output), Panel Kanan sebagai Keypad/Numpad (Input).
2.  **Keamanan Berlapis (Proxy):** Akses ke saldo dan transaksi dilindungi oleh *Proxy Pattern* yang mewajibkan login.
3.  **Dual-Mode Database:**
    * **Mode SQL:** Data tersimpan permanen di **PostgreSQL**.
    * **Mode Memory:** Jika database mati, otomatis *fallback* ke data dummy (RAM) agar aplikasi tetap bisa didemokan.
4.  **Fitur Lengkap:** Cek Saldo, Tarik Tunai, Setor Tunai, Transfer, dan Mutasi Rekening (Riwayat).

---

## 🏗 Arsitektur & Design Pattern
Proyek ini dibangun di atas pondasi OOP yang kuat:

| Pattern | Implementasi | Fungsi |
| :--- | :--- | :--- |
| **Singleton** | `BankDatabase` | Menjamin hanya ada satu koneksi database yang aktif. |
| **Proxy** | `ProxyAkun` | Mencegat akses ke `AkunAsli`. Memastikan user sudah login sebelum transaksi. |
| **State** | `MesinATM` & `StateATM` | Mengatur alur layar (Welcome -> Login -> Menu) tanpa `if-else` yang rumit. |

---

## 🚀 Cara Menjalankan

### Prasyarat
* Java JDK 17+
* Maven
* PostgreSQL (Opsional, untuk mode SQL)

### Setup Database (Mode SQL)
1.  Buka PostgreSQL / pgAdmin.
2.  Buat database baru bernama `db_atm_simulasi`.
3.  Jalankan script SQL yang ada di file **`database.sql`**.
4.  Sesuaikan password di `src/main/resources/database.properties`.

### Run Aplikasi
1.  Buka project di VS Code / IntelliJ.
2.  Jalankan file **`src/main/java/Main.java`**.
---

## 👥 Anggota Tim

| NIM | Nama | Peran Utama |
| :--- | :--- | :--- |
| **[241511009]** | **Faridha Zahiya** |
| **[241511026]** | **Suci Sulistiawati** |
| **[241511031]** | **Zainandhi Nur Fathurrohman** |

---
*Dibuat untuk memenuhi tugas besar mata kuliah PBO 2025.*