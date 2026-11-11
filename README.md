# 📚 Book App - Sistem Point of Sale (POS) & Inventaris Toko Buku

Book App adalah aplikasi desktop berbasis **Java Swing** yang dirancang khusus untuk memecahkan masalah pencatatan manual pada UMKM toko buku. Aplikasi ini mengintegrasikan manajemen stok barang dengan sistem kasir, sehingga setiap transaksi secara otomatis akan memperbarui jumlah stok fisik dan tercatat dalam laporan keuangan.

---

## ✨ Fitur Utama & Alur Kerja (Workflow)

### Fungsionalitas
- 🔐 **Autentikasi (Login):** Pembatasan akses ke dalam sistem khusus untuk Kasir atau Admin.
- 📦 **Manajemen Inventaris (CRUD):** Tambah, ubah, hapus, dan cari data buku berdasarkan judul atau penulis.
- 🛒 **Sistem Kasir:** Antarmuka transaksi kasir dengan perhitungan total belanja, uang tunai, dan kembalian otomatis.
- 📊 **Laporan Transaksi:** Riwayat lengkap penjualan beserta detail buku yang terjual.

### Alur Sistem
1. **Masuk:** Buka aplikasi ➔ Form Login ➔ Verifikasi Database ➔ Masuk ke Dashboard Utama.
2. **Kelola Stok:** Menu *Kelola Buku* ➔ Tampil tabel buku ➔ Klik Tambah/Edit/Hapus ➔ Simpan ➔ Tabel dan database diperbarui.
3. **Kasir/Transaksi:** Menu *Kasir* ➔ Cari buku & masukkan ke keranjang ➔ Subtotal dihitung otomatis ➔ Input uang tunai bayar ➔ Klik *Proses* ➔ Stok database berkurang, transaksi tersimpan, popup kembalian muncul.

---

## 🏗️ Arsitektur Class (Pola MVC)

Aplikasi ini menggunakan pola arsitektur **Model-View-Controller (MVC)** untuk memisahkan data, antarmuka, dan logika bisnis.

*   **`model` (Entitas Data):**
    *   `Buku.java` (id, judul, penulis, harga, stok, tahun_terbit)
    *   `Transaksi.java` (id_transaksi, datetime, total_belanja, tunai, id_user)
    *   `DetailTransaksi.java` (id_detail, id_transaksi, id_buku, jumlah, subtotal)
    *   `LogInventori.java` (id_log, id_buku, id_user, datetime, desc, type)
    *   `users` (id, username, password, role)
*   **`config` (Koneksi Database):**
    *   `DatabaseConnection.java` (Membuka koneksi JDBC ke MySQL)
*   **`controller` (Logika Bisnis):**
    *   `BukuController.java` (Metode CRUD dan pengurang stok)
    *   `TransaksiController.java` (Pembuatan ID transaksi otomatis dan simpan ke DB)
    *   `UserController.java` (Verifikasi login)
*   **`view` (Antarmuka GUI Swing):**
    *   `LoginFrame.java`, `MainFrame.java`, `BukuPanel.java`, `KasirPanel.java`, `LaporanPanel.java`

---

## 🗄️ Rancangan Database

Database bernama `db_bookapp` dengan 5 tabel utama:

### 1. Tabel `users` (Akses Login)
| Kolom | Tipe Data | Keterangan |
| :--- | :--- | :--- |
| `id_user` | INT (11) | Primary Key, Auto Increment |
| `username` | VARCHAR (50) | Nama pengguna |
| `password` | VARCHAR (255) | Kata sandi |
| `role` | VARCHAR (20) | "Admin" atau "Kasir" |

### 2. Tabel `buku` (Master Barang)
| Kolom | Tipe Data | Keterangan |
| :--- | :--- | :--- |
| `id_buku` | INT (11) | Primary Key |
| `judul` | VARCHAR (150) | Judul buku |
| `penulis` | VARCHAR (100) | Nama penulis |
| `harga` | DECIMAL (10,2) | Harga jual |
| `stok` | INT (11) | Sisa stok fisik |
| `tahun_terbit` | INT(4) | Kapan terbitnya |

### 3. Tabel `transaksi` (Header Pembelian)
| Kolom | Tipe Data | Keterangan |
| :--- | :--- | :--- |
| `id_transaksi` | INT (11) | Primary Key |
| `datetime` | DATETIME | Waktu transaksi |
| `total_belanja`| DECIMAL (10,2) | Total harga semua item |
| `tunai` | DECIMAL (10,2) | Uang pembayaran pelanggan |
| `id_user` | INT (11) | Foreign Key ➔ `users.id_user` |

### 4. Tabel `detail_transaksi` (Item Keranjang)
| Kolom | Tipe Data | Keterangan |
| :--- | :--- | :--- |
| `id_detail` | INT (11) | Primary Key, Auto Increment |
| `id_transaksi` | INT (11) | Foreign Key ➔ `transaksi.id_transaksi` |
| `id_buku` | INT(11) | Foreign Key ➔ `buku.id_buku` |
| `jumlah` | INT (5) | Kuantitas buku dibeli |
| `subtotal` | DECIMAL (10,2) | `jumlah` * `harga` saat transaksi |

### 5. Tabel `log_inventori` (log inventori)
| Kolom | Tipe Data | Keterangan |
| :--- | :--- | :--- |
| `id_log` | INT (11) | Primary Key, Auto Increment |
| `id_user` | INT (11) | Foreign Key ➔ `user.id_user` |
| `id_buku` | INT (11) | Foreign Key ➔ `buku.id_buku` |
| `datetime` | DATETIME | Kapan aksi dilakukan |
| `desc` | TEXT | Deskripsi aksi |
| `type` | ENUM(CREATE,UPDATE,DELETE) | Tipe aksi |
---

## 📊 Diagram
### Diagram Penggunaan
![Use Case Diagram](readme/use_case.png)

### Flowmap
```mermaid
flowchart TD
    subgraph Pelanggan [Pelanggan]
        P1[\"Pelanggan Masuk Toko"/]
        P2[\"Pelanggan Cari/Beli Buku"/]
        P3[/"Buku"/]
        P4[\"Memberikan duit"/]
        
        P5[\"Gagal Beli Buku"/]
        P6[\"Nangis"/]
        P7([End])
        
        P8[/"Buku dan kembalian <br> (jika ada)"/]
        P9[\"Senang"/]
        P10([End])
    end

    subgraph Kasir [Kasir]
        K1[Login]
        K2[/"Buku"/]
        K3[/"Inputkan data buku"/]
        K4[\"Beri tahu harga dan nagih duit"/]
        K5{"Duit cukup?"}
        
        K6[\"Beri tahu gak cukup"/]
        
        K7[\"Terima duit"/]
        K8["Menyelesaikan pembayaran <br> auto update stock <br> trans log auto updates"]
        K9[/"Buku dan kembalian <br> (jika ada)"/]
        
        K10[Cek log]
    end

    subgraph Admin [Admin]
        A1([Start])
        A2[Login]
        A3[/"CRUD Inventory <br> Inventory log auto updates"/]
        
        A4[Cek log]
        A5([End])
    end

    %% Hubungan Start -> Admin -> Kasir -> Pelanggan
    A1 --> A2
    A2 --> A3
    A3 --> K1
    K1 --> P1
    
    %% Siklus cari buku
    P1 --> P2
    P2 --> P3
    P3 --> K2
    
    %% Siklus transaksi kasir
    K2 --> K3
    K3 --> K4
    K4 --> P4
    P4 --> K5
    
    %% Percabangan: Tidak Cukup
    K5 -- Tidak --> K6
    K6 --> P5
    P5 --> P6
    P6 --> P7
    
    %% Percabangan: Cukup (Lanjut)
    K5 --> K7
    K7 --> K8
    K8 --> K9
    
    %% Distribusi kembalian dan penyelesaian
    K9 --> P8
    P8 --> P9
    P9 --> P10
    
    %% Sinkronisasi Log
    K9 --> K10
    K10 --> A4
    A4 --> A5
```
### Flowmap (2)
![Flowmap](readme/flowmap.png)

### Skema Tabel
```mermaid
erDiagram
    users {
        int id_user PK
        varchar username
        varchar password
        varchar role
    }

    buku {
        varchar id_buku PK
        varchar judul
        varchar penulis
        decimal harga
        int stok
        year tahun_terbit
    }

    transaksi {
        varchar id_transaksi PK
        datetime waktu
        decimal total_belanja
        decimal tunai
        int id_user FK
    }

    detail_transaksi {
        int id_detail PK
        varchar id_transaksi FK
        varchar id_buku FK
        int jumlah
        decimal subtotal
    }

    log_inventori {
        int id_log PK
        int id_user FK
        varchar id_buku FK
        datetime datetime
        text desc
        enum type 
    }

    users ||--o{ transaksi : "melayani"
    users ||--o{ log_inventori : "melakukan aksi"
    buku ||--o{ detail_transaksi : "termasuk dalam"
    transaksi ||--|{ detail_transaksi : "memiliki"
    buku ||--o{ log_inventori : "dicatat pada"
```

### Class Diagram
```mermaid
classDiagram
    %% ================= MODEL (Entitas Data) =================
    class Buku {
        -String id_buku
        -String judul
        -String penulis
        -double harga
        -int stok
        -int tahun_terbit
        +getters()
        +setters()
    }

    class Transaksi {
        -String id_transaksi
        -Date datetime
        -double total_belanja
        -double tunai
        -int id_user
        +getters()
        +setters()
    }

    class DetailTransaksi {
        -int id_detail
        -String id_transaksi
        -String id_buku
        -int jumlah
        -double subtotal
        +getters()
        +setters()
    }

    class LogInventori {
        -int id_log
        -String id_buku
        -int id_user
        -Date datetime
        -String desc
        -String type
        +getters()
        +setters()
    }

    %% ================= CONFIG (Koneksi Database) =================
    class DatabaseConnection {
        -Connection connection$+getConnection() Connection$
    }

    %% ================= CONTROLLER (Logika Bisnis) =================
    class BukuController {
        +insertBuku(Buku b) boolean
        +updateBuku(Buku b) boolean
        +deleteBuku(String id_buku) boolean
        +getAllBuku() List~Buku~
        +kurangiStok(String id_buku, int jumlah) boolean
    }

    class TransaksiController {
        +generateIdTransaksi() String
        +simpanTransaksi(Transaksi t, List~DetailTransaksi~ listDetail) boolean
        +getRiwayatTransaksi() List~Transaksi~
    }

    class UserController {
        +login(String username, String password) boolean
    }

    %% ================= VIEW (Antarmuka GUI) =================
    class LoginFrame {
        -JTextField txtUsername
        -JPasswordField txtPassword
        -JButton btnLogin
        +tampilkan()
    }

    class MainFrame {
        -JTabbedPane tabbedPane
        +tampilkan()
    }

    class BukuPanel {
        -JTable tabelBuku
        +refreshTabel()
    }

    class KasirPanel {
        -JTable tabelKeranjang
        -JTextField txtTotal
        -JTextField txtTunai
        +hitungSubtotal()
        +prosesPembayaran()
    }

    class LaporanPanel {
        -JTable tabelLaporan
        +refreshLaporan()
    }

    %% ================= RELASI & KETERGANTUNGAN =================

    %% Controller ke Model
    BukuController ..> Buku : uses
    BukuController ..> LogInventori : uses
    TransaksiController ..> Transaksi : uses
    TransaksiController ..> DetailTransaksi : uses

    %% Controller ke Config
    BukuController ..> DatabaseConnection : connects
    TransaksiController ..> DatabaseConnection : connects
    UserController ..> DatabaseConnection : connects

    %% View ke Controller
    LoginFrame --> UserController : calls
    BukuPanel --> BukuController : calls
    KasirPanel --> TransaksiController : calls
    KasirPanel --> BukuController : calls (cek/kurangi stok)
    LaporanPanel --> TransaksiController : calls

    %% Komposisi View (MainFrame memuat Panel-panel)
    MainFrame *-- BukuPanel : contains
    MainFrame *-- KasirPanel : contains
    MainFrame *-- LaporanPanel : contains
```


## 🛠️ Teknologi & Prasyarat Sistem

*   **Bahasa:** Java (JDK 8 / 11 / 17+)
*   **GUI:** Java Swing & AWT
*   **Database:** MySQL (XAMPP / Laragon)
*   **Driver:** MySQL Connector/J (JDBC)

---

## 🚀 Cara Instalasi & Menjalankan

1. **Clone Repository:**
   ```bash
   git clone [https://github.com/username-anda/book-app.git](https://github.com/username-anda/book-app.git)
