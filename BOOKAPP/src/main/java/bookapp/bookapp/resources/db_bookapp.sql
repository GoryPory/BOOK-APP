CREATE DATABASE IF NOT EXISTS db_bookapp;
USE db_bookapp;

CREATE TABLE IF NOT EXISTS users (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS buku (
    id_buku INT AUTO_INCREMENT PRIMARY KEY,
    judul VARCHAR(150) NOT NULL,
    penulis VARCHAR(100) NOT NULL,
    harga DECIMAL(10, 2) NOT NULL,
    stok INT NOT NULL DEFAULT 0,
    tahun_terbit INT(4) NOT NULL
);

CREATE TABLE IF NOT EXISTS transaksi (
    id_transaksi VARCHAR(50) PRIMARY KEY,
    datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_belanja DECIMAL(10, 2) NOT NULL,
    tunai DECIMAL(10, 2) NOT NULL,
    id_user INT NOT NULL,
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS detail_transaksi (
    id_detail INT AUTO_INCREMENT PRIMARY KEY,
    id_transaksi VARCHAR(50) NOT NULL,
    id_buku INT NOT NULL,
    jumlah INT NOT NULL DEFAULT 1,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_transaksi) REFERENCES transaksi(id_transaksi) ON DELETE CASCADE,
    FOREIGN KEY (id_buku) REFERENCES buku(id_buku) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS log_inventori (
    id_log INT AUTO_INCREMENT PRIMARY KEY,
    id_user INT NOT NULL,
    id_buku INT NOT NULL,
    datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `desc` TEXT NOT NULL,
    type ENUM('CREATE', 'UPDATE', 'DELETE') NOT NULL,
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE,
    FOREIGN KEY (id_buku) REFERENCES buku(id_buku) ON DELETE CASCADE
);

INSERT INTO users (username, password, role) VALUES
    ('admin', 'admin123', 'Admin'),
    ('kasir', 'kasir123', 'Kasir');

INSERT INTO buku (judul, penulis, harga, stok, tahun_terbit) VALUES
    ('Laskar Pelangi', 'Andrea Hirata', 85000.00, 50, 2005),
    ('Bumi Manusia', 'Pramoedya Ananta Toer', 110000.00, 30, 1980),
    ('Atomic Habits', 'James Clear', 125000.00, 100, 2018);

INSERT INTO transaksi (id_transaksi, total_belanja, tunai, id_user) VALUES
    ('TRX-001', 170000.00, 200000.00, 2),
    ('TRX-002', 125000.00, 125000.00, 2);

INSERT INTO detail_transaksi (id_transaksi, id_buku, jumlah, subtotal) VALUES
    ('TRX-001', 1, 2, 170000.00),
    ('TRX-002', 3, 1, 125000.00);