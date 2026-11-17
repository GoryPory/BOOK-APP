/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookapp.bookapp.model;

/**
 *
 * @author radit
 */
import java.util.List;

public class Transaksi implements Base<Transaksi, String> { // Tipe ID Transaksi adalah String (VARCHAR)

    @Override
    public void create(Transaksi item) {
        // TODO: Implementasi simpan transaksi baru
    }

    @Override
    public List<Transaksi> readAll() {
        // TODO: Implementasi tampilkan semua riwayat transaksi
        return null;
    }

    @Override
    public List<Transaksi> search(String keyword) {
        // TODO: (SURE/Opsional) Pencarian transaksi
        return null;
    }

    @Override
    public void delete(String id) {
        // TODO: (SURE) Implementasi Hapus Transaksi
    }
}